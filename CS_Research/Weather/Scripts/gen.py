import openmeteo_requests

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator
from pathlib import Path
import file_ops

import requests_cache
from retry_requests import retry

def get_weather_data() -> pd.DataFrame:
	# Setup the Open-Meteo API client with cache and retry on error
	cache_session = requests_cache.CachedSession('.cache', expire_after = -1)
	retry_session = retry(cache_session, retries = 5, backoff_factor = 0.2)
	openmeteo = openmeteo_requests.Client(session = retry_session)

	# Make sure all required weather variables are listed here
	# The order of variables in hourly or daily is important to assign them correctly below
	url = "https://archive-api.open-meteo.com/v1/archive"
	params = {
		"latitude": 47.6597,
		"longitude": -117.4291,
		"start_date": "1970-01-01",
		"end_date": "2025-12-31",
		"daily": ["temperature_2m_min","temperature_2m_max", "temperature_2m_mean", "shortwave_radiation_sum"],
		"temperature_unit": "fahrenheit",
		"wind_speed_unit": "mph",
	}
	responses = openmeteo.weather_api(url, params = params)

	# Process first location. Add a for-loop for multiple locations or weather models
	response = responses[0]
	daily = response.Daily()

	daily_data = {
		"date": pd.date_range(
			start = pd.to_datetime(daily.Time(), unit = "s", utc = True),
			end =  pd.to_datetime(daily.TimeEnd(), unit = "s", utc = True),
			freq = pd.Timedelta(seconds = daily.Interval()),
			inclusive = "left"
		)
	}

	daily_data["temp_min"] = daily.Variables(0).ValuesAsNumpy()
	daily_data["temp_max"] = daily.Variables(1).ValuesAsNumpy()
	daily_data["temp_mean"] = daily.Variables(2).ValuesAsNumpy()
	daily_data["rad_sum"] = daily.Variables(3).ValuesAsNumpy()
	
	daily_dataframe = pd.DataFrame(data = daily_data)
	daily_dataframe["year"] = daily_dataframe["date"].dt.year

	return daily_dataframe

def get_annual_data(daily: pd.DataFrame) -> pd.DataFrame:
	return (
		daily.groupby("year")
		.agg(
			radiation_total = ("rad_sum", "sum"),
			mean_temperature = ("temp_mean", "mean"),
			mean_daily_max=("temp_max", "mean"),
			mean_daily_min=("temp_min", "mean"),
			days=("date", "size")
		)
		.reset_index()
	)

def get_monthly_baseline(daily: pd.DataFrame, start_year: int, end_year: int) -> pd.DataFrame:
	baseline = daily.loc[daily["year"].between(start_year, end_year)].copy()
	baseline["month"] = baseline["date"].dt.month

	return baseline.groupby("month").agg(
		high_mean=("temp_max", "mean"),
		high_std=("temp_max", "std"),
		low_mean=("temp_min", "mean"),
		low_std=("temp_min", "std"),
		rad_mean=("rad_sum", "mean"),
		rad_std=("rad_sum", "std")
	)

def get_daily_outliers(daily: pd.DataFrame, monthly_baseline: pd.DataFrame) -> pd.DataFrame:
	result = daily.copy()
	result["month"] = result["date"].dt.month
	result = result.join(monthly_baseline, on="month")

	result["too_hot"] = (result["temp_max"] > result["high_mean"] + result["high_std"])
	result["too_cold"] = (result["temp_min"] < result["low_mean"] - result["low_std"])
	result["too_bright"] = (result["rad_sum"] > result["rad_mean"] + result["rad_std"])
	result["too_dim"] = (result["rad_sum"] < result["rad_mean"] - result["rad_std"])

	return result

def get_annual_outliers(baseline: pd.DataFrame) -> pd.DataFrame:
	annual_outliers = (
		baseline.groupby("year")
		.agg(
			days=("date", "size"),
			hot=("too_hot", "sum"),
			cold=("too_cold", "sum"),
			bright=("too_bright", "sum"),
			dim=("too_dim", "sum")
		)
		.reset_index()
	)
	return annual_outliers


def plot_with_trend(ax, x, y, data_label, trend_label):
	x = np.asarray(x)
	y = np.asarray(y)
	slope, intercept = np.polyfit(x, y, 1)

	data_line = ax.plot(x, y, "o-", label=data_label)
	ax.plot(
		x,
		slope * x + intercept,
		"--",
		label=trend_label
	)
	return slope

def plot_annual(annual: pd.DataFrame, figures_dir: Path):
	X = annual["year"].to_numpy()
	radY = annual["radiation_total"].to_numpy()
	avgY = annual["mean_temperature"].to_numpy()
	maxY = annual["mean_daily_max"].to_numpy()
	minY = annual["mean_daily_min"].to_numpy()

	fig, (ax_rad, ax_temp), = plt.subplots(
		2, 1, sharex=True, figsize=(10, 7), constrained_layout=True
	)

	plot_with_trend(ax_rad, X, radY, "Annual total", "Radiation trend")

	ax_rad.set(
		title="Annual Radiation",
		ylabel="Radiation (MJ/m²)"
	)
	ax_rad.legend()

	plot_with_trend(ax_temp, X, avgY, "Annual mean temperature", "Mean-temperature trend")
	plot_with_trend(ax_temp, X, maxY, "Average daily high", "Daily-high trend")
	plot_with_trend(ax_temp, X, minY, "Average daily low", "Daily-low trend")

	ax_temp.set(
		title="Annual Temperature",
		xlabel="Year",
		ylabel="Temperature (°F)"
	)
	ax_temp.xaxis.set_major_locator(MaxNLocator(integer=True, nbins=10))
	ax_temp.legend()

	fig.savefig(file_ops.next_path(figures_dir / "annual_weather.png"), format="png", dpi=500, bbox_inches="tight")
	plt.close(fig)

def plot_outliers(outliers: pd.DataFrame, figures_dir: Path):
	for name in ("hot", "cold", "bright", "dim"):
		outliers[f"{name}_pct"] = 100 * outliers[name] / outliers["days"]

	x = outliers["year"].to_numpy()

	fig, (ax_temp, ax_rad) = plt.subplots(
		2, 1, sharex=True, figsize=(10, 7), constrained_layout=True
	)
	plot_with_trend(ax_temp, x, outliers["hot_pct"], "Unusually Hot", "Hot Trend")
	plot_with_trend(ax_temp, x, outliers["cold_pct"], "Unusually Cold", "Cold Trend")
	ax_temp.set(title="Unusual Temperature Days", ylabel="Days per year (%)")
	ax_temp.legend()

	plot_with_trend(ax_rad, x, outliers["dim_pct"], "Unusually Dim", "Dim Trend")
	plot_with_trend(ax_rad, x, outliers["bright_pct"], "Unusually Bright", "Bright Trend")
	ax_rad.set(title="Unusual Radiation Days", ylabel="Days per year (%)")
	ax_rad.xaxis.set_major_locator(MaxNLocator(integer=True, nbins=10))
	ax_rad.legend()
	fig.savefig(file_ops.next_path(figures_dir / "outliers.png"), format="png", dpi=500, bbox_inches="tight")
	plt.close(fig)

def plot_scatter(daily_outliers: pd.DataFrame, figures_dir: Path) -> None:
	points = pd.DataFrame({
		"rad_z":(daily_outliers["rad_sum"] - daily_outliers["rad_mean"]) / daily_outliers["rad_std"],
		"high_z":(daily_outliers["temp_max"] - daily_outliers["high_mean"]) / daily_outliers["high_std"]
	}).dropna()

	x = points["rad_z"].to_numpy()
	y = points["high_z"].to_numpy()
	slope, intercept = np.polyfit(x, y, 1)
	correlation = points["rad_z"].corr(points["high_z"])

	fig, ax, = plt.subplots(figsize=(8, 6), constrained_layout=True)
	ax.scatter(x, y, label="Daily observations")

	fit_x = np.linspace(x.min(), x.max(), 100)
	ax.plot(fit_x, slope * fit_x + intercept, "--", label=f"Linear fit (r={correlation:.2f})")

	for threshold in (-1, 0, 1):
		ax.axhline(threshold, color="gray", linewidth=0.7, alpha=0.5)
		ax.axvline(threshold, color="gray", linewidth=0.7, alpha=0.5)

	ax.set(
		title="Daily Radiation vs Daily High Temperature",
		xlabel="Radiation anomaly (standard deviations)",
		ylabel="High Temperature anomaly (standard deviations)"
	)

	ax.legend()
	hb = ax.hexbin(x, y, gridsize=45, mincnt=1, cmap="viridis")
	fig.colorbar(hb, ax=ax, label="Number of days")
	fig.savefig(file_ops.next_path(figures_dir / "scatter.png"), format="png", dpi=500, bbox_inches="tight")
	plt.close(fig)

def plot_lag(daily_outliers: pd.DataFrame, figures_dir: Path) -> None:
	points = daily_outliers[
		["date", "rad_sum", "rad_mean", "rad_std", "temp_max", "high_mean", "high_std"]
	].copy().sort_values("date").reset_index(drop=True)

	points["rad_z"] = (points["rad_sum"] - points["rad_mean"]) / points["rad_std"]
	points["high_z"] = (points["temp_max"] - points["high_mean"]) / points["high_std"]

	lags = range(3)
	correlations = [
		points["rad_z"].corr(points["high_z"].shift(-lag))
		for lag in lags
	]

	fig, ax = plt.subplots()
	ax.plot(list(lags), correlations, "o-")
	ax.set(
		title="Radiation Lead vs Temperature Anomaly Correlation",
		xlabel="Lag (days)",
		ylabel="Correlation"
	)
	ax.set_xticks(list(lags))
	fig.savefig(file_ops.next_path(figures_dir / "lag.png"), format="png", dpi=500, bbox_inches="tight")
	plt.close(fig)
	

def main():
	print("Gathering data")
	daily = get_weather_data()

	print("Processing data")
	annual = get_annual_data(daily)
	monthly_baseline = get_monthly_baseline(daily, 1985, 2014)
	daily_outliers = get_daily_outliers(daily, monthly_baseline)
	annual_outliers = get_annual_outliers(daily_outliers)
	#annual_outliers = summarize_by_year(daily_outliers)

	figures_dir = Path(__file__).resolve().parent.parent / "Figures"
	figures_dir.mkdir(parents=True, exist_ok=True)

	print("Generating plots")
	plot_annual(annual, figures_dir)
	plot_outliers(annual_outliers, figures_dir)
	plot_scatter(daily_outliers, figures_dir)
	plot_lag(daily_outliers, figures_dir)

	print("Deduplicating figures")
	file_ops.deduplicate(figures_dir / "annual_weather.png")
	file_ops.deduplicate(figures_dir / "outliers.png")
	file_ops.deduplicate(figures_dir / "scatter.png")
	file_ops.deduplicate(figures_dir / "lag.png")

	print("Done")
if __name__ == "__main__":
	main()

