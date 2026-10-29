import openmeteo_requests

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from pathlib import Path

import requests_cache
from retry_requests import retry

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
	"start_date": "2006-10-12",
	"end_date": "2026-10-26",
	"daily": ["temperature_2m_max", "temperature_2m_mean", "shortwave_radiation_sum"],
	"timezone": "America/Los_Angeles",
	"temperature_unit": "fahrenheit",
	"wind_speed_unit": "mph",
}
responses = openmeteo.weather_api(url, params = params)

# Process first location. Add a for-loop for multiple locations or weather models
response = responses[0]
print(f"Coordinates: {response.Latitude()}°N {response.Longitude()}°E")
print(f"Elevation: {response.Elevation()} m asl")
print(f"Timezone: {response.Timezone()}{response.TimezoneAbbreviation()}")
print(f"Timezone difference to GMT+0: {response.UtcOffsetSeconds()}s")

# Process daily data. The order of variables needs to be the same as requested.
daily = response.Daily()
daily_temperature_2m_max = daily.Variables(0).ValuesAsNumpy()
daily_temperature_2m_mean = daily.Variables(1).ValuesAsNumpy()
daily_shortwave_radiation_sum = daily.Variables(2).ValuesAsNumpy()

daily_data = {
	"date": pd.date_range(
		start = pd.to_datetime(daily.Time(), unit = "s", utc = True),
		end =  pd.to_datetime(daily.TimeEnd(), unit = "s", utc = True),
		freq = pd.Timedelta(seconds = daily.Interval()),
		inclusive = "left"
	).tz_convert(response.Timezone().decode())
}

daily_data["temperature_2m_max"] = daily_temperature_2m_max
daily_data["temperature_2m_mean"] = daily_temperature_2m_mean
daily_data["shortwave_radiation_sum"] = daily_shortwave_radiation_sum

daily_dataframe = pd.DataFrame(data = daily_data)

fig, ax, = plt.subplots()
plt.title("Daily Radiation")
ax.plot(
    daily_dataframe["date"],
    daily_dataframe["shortwave_radiation_sum"],
    marker="."
)
ax.set(
    title="Daily Shortwave Radiation Sum",
    xlabel="Date",
    ylabel="Shortwave Radiation Sum (MJ/m²)"
)
fig.autofmt_xdate()

figures_dir = Path(__file__).resolve().parent.parent / "Figures"
figures_dir.mkdir(parents=True, exist_ok=True)
fig.savefig(figures_dir / "shortwave_radiation.png", format="png", dpi=500, bbox_inches="tight")
plt.close(fig)
