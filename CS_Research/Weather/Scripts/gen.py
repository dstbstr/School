import openmeteo_requests

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator
from pathlib import Path

import requests_cache
from retry_requests import retry

def next_image_path(path: Path) -> Path:
	highest = 1 if path.exists() else 0
	prefix = f"{path.stem}_"

	for candidate in path.parent.glob(f"{prefix}*{path.suffix}"):
		suffix = candidate.stem[len(prefix):]
		if candidate.stem.startswith(prefix) and suffix.isdigit():
			highest = max(highest, int(suffix))

		if highest == 0:
			return path

	return path.with_name(f"{path.stem}_{highest+1}{path.suffix}")
	
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
print(f"Coordinates: {response.Latitude()}°N {response.Longitude()}°E")
print(f"Elevation: {response.Elevation()} m asl")

# Process daily data. The order of variables needs to be the same as requested.
daily = response.Daily()
daily_temperature_2m_min = daily.Variables(0).ValuesAsNumpy()
daily_temperature_2m_max = daily.Variables(1).ValuesAsNumpy()
daily_temperature_2m_mean = daily.Variables(2).ValuesAsNumpy()
daily_shortwave_radiation_sum = daily.Variables(3).ValuesAsNumpy()

daily_data = {
	"date": pd.date_range(
		start = pd.to_datetime(daily.Time(), unit = "s", utc = True),
		end =  pd.to_datetime(daily.TimeEnd(), unit = "s", utc = True),
		freq = pd.Timedelta(seconds = daily.Interval()),
		inclusive = "left"
	)
}

daily_data["temperature_2m_min"] = daily_temperature_2m_min
daily_data["temperature_2m_max"] = daily_temperature_2m_max
daily_data["temperature_2m_mean"] = daily_temperature_2m_mean
daily_data["shortwave_radiation_sum"] = daily_shortwave_radiation_sum

daily_dataframe = pd.DataFrame(data = daily_data)
daily_dataframe["year"] = daily_dataframe["date"].dt.year

### Baseline
baseline = daily_dataframe.loc[daily_dataframe["year"].between(1985, 2014)].copy()
baseline["month"] = baseline["date"].dt.month

monthly_baseline = baseline.groupby("month").agg(
	high_mean=("temperature_2m_max", "mean"),
	high_std=("temperature_2m_max", "std"),
	low_mean=("temperature_2m_min", "mean"),
	low_std=("temperature_2m_min", "std"),
	rad_mean=("shortwave_radiation_sum", "mean"),
	rad_std=("shortwave_radiation_sum", "std")
)

daily_dataframe["month"] = daily_dataframe["date"].dt.month
daily = daily_dataframe.join(monthly_baseline, on="month")
daily["too_hot"] = (
	daily["temperature_2m_max"] > daily["high_mean"] + daily["high_std"]
)
daily["too_cold"] = (
	daily["temperature_2m_min"] < daily["low_mean"] - daily["low_std"]
)
daily["too_bright"] = (
	daily["shortwave_radiation_sum"] > daily["rad_mean"] + daily["rad_std"]
)
daily["too_dim"] = (
	daily["shortwave_radiation_sum"] < daily["rad_mean"] - daily["rad_std"]
)


### Annual Weather Plots

annual = (
    daily_dataframe.groupby("year")
    .agg(
        radiation_total = ("shortwave_radiation_sum", "sum"),
        mean_temperature = ("temperature_2m_mean", "mean"),
        mean_daily_max=("temperature_2m_max", "mean"),
        mean_daily_min=("temperature_2m_min", "mean"),
        days=("date", "size")
	)
    .reset_index()
)

X = annual["year"].to_numpy()
radY = annual["radiation_total"].to_numpy()
avgY = annual["mean_temperature"].to_numpy()
maxY = annual["mean_daily_max"].to_numpy()
minY = annual["mean_daily_min"].to_numpy()

radSlope, radIntercept = np.polyfit(X, radY, 1)
avgSlope, avgIntercept = np.polyfit(X, avgY, 1)
maxSlope, maxIntercept = np.polyfit(X, maxY, 1)
minSlope, minIntercept = np.polyfit(X, minY, 1)

fig, (ax_rad, ax_temp), = plt.subplots(
    2, 1, sharex=True, figsize=(10, 7), constrained_layout=True
)

ax_rad.plot(X, radY, "o-", label="Annual total")
ax_rad.plot(X, radSlope * X + radIntercept, "--", label="Linear trend")
ax_rad.set(
    title="Annual Radiation",
    ylabel="Radiation (MJ/m²)"
)
ax_rad.legend()

ax_temp.plot(X, avgY, "o-", label="Annual mean temperature")
ax_temp.plot(X, avgSlope * X + avgIntercept, "--", label="Mean-temperature trend")
ax_temp.plot(X, maxY, "o-", label="Average daily high")
ax_temp.plot(X, maxSlope * X + maxIntercept, "--", label="Daily-high trend")
ax_temp.plot(X, minY, "o-", label="Average daily low")
ax_temp.plot(X, minSlope * X + minIntercept, "--", label="Daily-low trend")
ax_temp.set(
    title="Annual Mean Temperature",
    xlabel="Year",
    ylabel="Temperature (°F)"
)
ax_temp.xaxis.set_major_locator(MaxNLocator(integer=True, nbins=10))
ax_temp.legend()

figures_dir = Path(__file__).resolve().parent.parent / "Figures"
figures_dir.mkdir(parents=True, exist_ok=True)
fig.savefig(next_image_path(figures_dir / "annual_weather.png"), format="png", dpi=500, bbox_inches="tight")
plt.close(fig)

### Annual Weather Stats
# annual_stats = (
#     daily_dataframe.groupby(daily_dataframe["date"].dt.year)
#     .agg(
#         radiation_mean=("shortwave_radiation_sum", "mean"),
#         radiation_std=("shortwave_radiation_sum", "std"),
#         temp_mean=("temperature_2m_mean", "mean"),
#         temp_std=("temperature_2m_mean", "std"),
#         high_mean=("temperature_2m_max", "mean"),
#         high_std=("temperature_2m_max", "std")
# 	)
#     .rename_axis("year")
#     .reset_index()
# )

# X_stats = annual_stats["year"].to_numpy()

# fig, (ax_rad, ax_temp) = plt.subplots(
#     2, 1, sharex=True, figsize=(10, 7), constrained_layout=True
# )

# ax_rad.errorbar(
#     X_stats,
#     annual_stats["radiation_mean"],
#     yerr=annual_stats["radiation_std"],
#     fmt="o-",
#     capsize=2,
#     label="Mean daily radiation ± SD"
# )
# ax_rad.set(title="Within-year Daily Variation", ylabel="Radiation (MJ/m²)")
# ax_rad.legend()

# ax_temp.errorbar(
#     X_stats,
#     annual_stats["temp_mean"],
#     yerr=annual_stats["temp_std"],
#     fmt="o-",
#     capsize=2,
#     label="Mean daily temperature ± SD"
# )

# ax_temp.errorbar(
#     X_stats,
#     annual_stats["high_mean"],
#     yerr=annual_stats["high_std"],
#     fmt="o-",
#     capsize=2,
#     label="Mean daily high ± SD"
# )
# ax_temp.set(xlabel="year", ylabel="Temperature (°F)")
# ax_temp.xaxis.set_major_locator(MaxNLocator(integer=True, nbins=10))
# ax_temp.legend()

# fig.savefig(next_image_path(figures_dir / "annual_stats.png"), format="png", dpi=500, bbox_inches="tight")

### Outlier plot
annual_outliers = (
	daily.groupby("year")
	.agg(
		days=("date", "size"),
		hot=("too_hot", "sum"),
		cold=("too_cold", "sum"),
		bright=("too_bright", "sum"),
		dim=("too_dim", "sum")
	)
	.reset_index()
)

for name in ("hot", "cold", "bright", "dim"):
	annual_outliers[f"{name}_pct"] = 100 * annual_outliers[name] / annual_outliers["days"]

outlier_X = annual_outliers["year"].to_numpy()
hotSlope, hotIntercept = np.polyfit(outlier_X, annual_outliers["hot_pct"], 1)
coldSlope, coldIntercept = np.polyfit(outlier_X, annual_outliers["cold_pct"], 1)
brightSlope, brightIntercept = np.polyfit(outlier_X, annual_outliers["bright_pct"], 1)
dimSlope, dimIntercept = np.polyfit(outlier_X, annual_outliers["dim_pct"], 1)


fig_outliers, (ax_temp, ax_rad) = plt.subplots(
	2, 1, sharex=True, figsize=(10, 7), constrained_layout=True
)
ax_temp.plot(annual_outliers["year"], annual_outliers["cold_pct"], "o-", label="Unusually Cold")
ax_temp.plot(annual_outliers["year"], annual_outliers["hot_pct"], "o-", label="Unusually Hot")
ax_temp.plot(annual_outliers["year"], hotSlope * outlier_X + hotIntercept, "--", label="Hot Trend")
ax_temp.plot(annual_outliers["year"], coldSlope * outlier_X + coldIntercept, "--", label="Cold Trend")
ax_temp.set(title="Unusual Temperature Days", ylabel="Days per year (%)")
ax_temp.legend()

ax_rad.plot(annual_outliers["year"], annual_outliers["dim_pct"], "o-", label="Unusually Dim")
ax_rad.plot(annual_outliers["year"], annual_outliers["bright_pct"], "o-", label="Unusually Bright")
ax_rad.plot(annual_outliers["year"], dimSlope * outlier_X + dimIntercept, "--", label="Dim Trend")
ax_rad.plot(annual_outliers["year"], brightSlope * outlier_X + brightIntercept, "--", label="Bright Trend")
ax_rad.set(title="Unusual Radiation Days", ylabel="Days per year (%)")
ax_rad.xaxis.set_major_locator(MaxNLocator(integer=True, nbins=10))
ax_rad.legend()
fig_outliers.savefig(next_image_path(figures_dir / "outliers.png"), format="png", dpi=500, bbox_inches="tight")
plt.close(fig_outliers)

### Scatter plot
daily["radiation_z"] = (
	daily["shortwave_radiation_sum"] - daily["rad_mean"]
) / daily["rad_std"]

daily["high_z"] = (
	daily["temperature_2m_max"] - daily["high_mean"]
) / daily["high_std"]


#plt.close(fig_outliers)
