package com.nick.android.rcvenues.activities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.Venue;
import com.nick.android.rcvenues.Weather;
import com.nick.android.rcvenues.WeatherRequest;
import com.nick.android.rcvenues.database.DatabaseHandler;
import com.nick.android.rcvenues.weather.WeatherUpdate;

public class WeatherActivity extends SherlockActivity {


	private int venueID;
	private DatabaseHandler dbHandler;
	private TextView weather_description, temp_tv, wind_tv, hum_tv, pre_tv,
			venue_name, last_checked_tv, wind_label, hum_label, pre_label;
	private ImageView weatherIcon;
	private SharedPreferences prefs;
	private String units;
	private Typeface robotoMed;
	private Typeface robotoThin;
	private SimpleDateFormat dateFormat;
	private Calendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		cal = Calendar.getInstance();

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.weather);
		setProgress(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Weather");
		robotoMed = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");
		robotoThin = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		units = prefs.getString("unitsPref", "Metric");

		dbHandler = new DatabaseHandler(this);
		venueID = this.getIntent().getIntExtra("id", -1);

		venue_name = (TextView) findViewById(R.id.venueName2);
		weather_description = (TextView) findViewById(R.id.weatherDescription);
		temp_tv = (TextView) findViewById(R.id.temp_tv);
		wind_tv = (TextView) findViewById(R.id.wind_tv);
		hum_tv = (TextView) findViewById(R.id.hum_tv);
		pre_tv = (TextView) findViewById(R.id.pre_tv);
		last_checked_tv = (TextView) findViewById(R.id.lastChecked);
		wind_label = (TextView) findViewById(R.id.wind_label);
		hum_label = (TextView) findViewById(R.id.hum_label);
		pre_label = (TextView) findViewById(R.id.pre_label);
		weatherIcon = (ImageView) findViewById(R.id.btn_weather);

		venue_name.setTypeface(robotoMed);
		weather_description.setTypeface(robotoMed);
		temp_tv.setTypeface(robotoThin);
		wind_tv.setTypeface(robotoMed);
		hum_tv.setTypeface(robotoMed);
		pre_tv.setTypeface(robotoMed);
		last_checked_tv.setTypeface(robotoThin);

		Venue v = dbHandler.getVenueInfo(venueID);
		venue_name.setText(v.getName());
		temp_tv.setText("Updating..");
		wind_tv.setText("Updating..");
		hum_tv.setText("Updating..");
		pre_tv.setText("Updating..");
		weather_description.setText("Updating..");

		getWeather(v.getLatitude(), v.getLongitude());

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void getWeather(double lat, double longi) {
			GetWeatherTask task = new GetWeatherTask();
			task.execute(new double[] { longi, lat });
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return true;
		}
	}

	public void setProgress(boolean status) {
		setSupportProgressBarIndeterminateVisibility(status);
	}

	private class GetWeatherTask extends AsyncTask<double[], Void, Weather> {

		@Override
		protected Weather doInBackground(double[]... params) {
			try {
				double[] coordinates = params[0];
				return WeatherUpdate.getCurrentWeather(coordinates[0], coordinates[1]);
			} catch(Exception e) {
				Toast.makeText(
						getBaseContext(),
						"Unable to retrieve weather information. Check your internet connection and try again.",
						Toast.LENGTH_LONG).show();
				finish();
				return null;
			}
			

		}

		@Override
		protected void onPostExecute(Weather weather) {
			setProgress(false);
			if (weather != null) {
				weather_description.setText(weather.description);
				last_checked_tv.setText("Updated: "
						+ dateFormat.format(cal.getTime()));
				if (units.equals("Metric")) {
					temp_tv.setText(weather.temperatureC + "¡ C");
					wind_tv.setText(weather.windSpeedKmph + "Km/h "
							+ weather.windDirectionPoints);
				} else {
					temp_tv.setText(weather.temperatureF + "¡ F");
					wind_tv.setText(weather.windSpeedMph + "Mph "
							+ weather.windDirectionPoints);
				}
				hum_tv.setText(weather.humidity + "%");
				pre_tv.setText(weather.precipitation + "mm");
				String[] iconNameparts = weather.iconUrl.split("/");
				String iconName = iconNameparts[iconNameparts.length-1];
				iconName = iconName.substring(0, iconName.length()-4);
				int imageResource = getResources().getIdentifier(
						"drawable/" + iconName, null, getPackageName());
				Drawable image = getResources().getDrawable(imageResource);
				weatherIcon.setImageDrawable(image);
			} else {
				Toast.makeText(
						getBaseContext(),
						"Unable to retrieve weather information. Check your internet connection and try again.",
						Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}
}
