package com.nick.android.rcvenues.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nick.android.rcvenues.Weather;

public class WeatherUpdate {
	private final static String API_KEY = "d515de9a03145205120805";

	public static Weather getCurrentWeather(double longi, double lati) {

		URL request = null;
		URLConnection conn = null;
		BufferedReader in = null;
		char[] arr = new char[2048];
		try {
			request = new URL(
					"http://free.worldweatheronline.com/feed/weather.ashx?"
							+ "q=" + lati + "," + longi
							+ "&format=json&num_of_days=2&" + "key=" + API_KEY);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			conn = request.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			in.read(arr);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String str = new String(arr);

		try {
			return parse(str);
		} catch (JSONException e) {

			return null;
		}
	}

	private static Weather parse(String response) throws JSONException {
		JSONObject obj = new JSONObject(response);
		if (obj.isNull("data")) {
			return null;
		} else {
			JSONObject data = obj.getJSONObject("data");
			JSONArray currentCondition = data.getJSONArray("current_condition");
			return parseWeather(currentCondition.getJSONObject(0));
		}

	}

	private static Weather parseWeather(JSONObject weather)
			throws JSONException {
		Weather w = new Weather();
		w.humidity = weather.optString("humidity");
		w.temperatureC = weather.optString("temp_C");
		w.temperatureF = weather.optString("temp_F");
		w.minTempC = weather.optString("tempMinC");
		w.minTempF = weather.optString("tempMinF");
		w.maxTempC = weather.optString("tempMaxC");
		w.maxTempF = weather.optString("tempMaxF");
		w.windDirectionDegrees = weather.getString("winddirDegree");
		w.windDirectionPoints = weather.getString("winddir16Point");
		w.windSpeedKmph = weather.getString("windspeedKmph");
		w.windSpeedMph = weather.getString("windspeedMiles");
		w.precipitation = weather.getString("precipMM");
		JSONArray description = weather.getJSONArray("weatherDesc");
		w.description = description.getJSONObject(0).getString("value");

		JSONArray iconUrl = weather.getJSONArray("weatherIconUrl");
		w.iconUrl = iconUrl.getJSONObject(0).getString("value");
		return w;
	}
	
}