package com.nick.android.rcvenues.activities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.Venue;
import com.nick.android.rcvenues.Weather;
import com.nick.android.rcvenues.WeatherRequest;
import com.nick.android.rcvenues.database.DatabaseHandler;

public class WeatherActivity extends SherlockActivity {
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
    private TextView weatherTv;
    private int venueID;
    private DatabaseHandler dbHandler;
    private TextView weather_description, temp_tv,
    				 wind_tv, hum_tv, pre_tv,
    				 venue_name;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Weather");	
        
		dbHandler = new DatabaseHandler(this);
		venueID = this.getIntent().getIntExtra("id", -1);
		
		venue_name = (TextView) findViewById(R.id.venueName2);
		weather_description = (TextView) findViewById(R.id.weatherDescription);
		temp_tv = (TextView) findViewById(R.id.temp_tv);
		wind_tv  = (TextView) findViewById(R.id.wind_tv);
		hum_tv = (TextView) findViewById(R.id.hum_tv);
		pre_tv  = (TextView) findViewById(R.id.pre_tv);
		
		Venue v = dbHandler.getVenueInfo(venueID);
		venue_name.setText(v.getName());
		temp_tv.setText("Updating..");
		wind_tv.setText("Updating..");
		hum_tv.setText("Updating..");
		pre_tv.setText("Updating..");
	
		getWeather(v.getLatitude(), v.getLongitude());
			
	}
	
	
	private void getWeather(double lat, double longi) {
		GetWeatherTask task = new GetWeatherTask();
		task.execute(new double[] {lat, longi} );
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
	
	private class GetWeatherTask extends AsyncTask<double[], Void, Weather> {
		

		@Override
		protected Weather doInBackground(double[]... params) {
			try {
				socket = new Socket(InetAddress.getByName("50.56.218.223"), 8080);
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				double[] pos = params[0];
				oos.writeObject(new WeatherRequest(pos[0], pos[1]));
				oos.flush();
				Object obj = ois.readObject();
				
				if(obj != null ) {
					return (Weather) obj;
				} else {
					return null;
				}
				
			} catch (UnknownHostException e) {
				System.err.println(e.getMessage());
				return null;
			} catch (IOException e) {
				System.err.println(e.getMessage());
				return null;
			} catch (ClassNotFoundException e) {
				System.err.println(e.getMessage());
				return null;
			}
		}
		
		@Override
        protected void onPostExecute(Weather weather){
			if(weather != null) {
				weather_description.setText(weather.description);
				temp_tv.setText(weather.temperatureC + "C");
				wind_tv.setText(weather.windSpeedKmph + "Km/h " + weather.windDirectionPoints);
				hum_tv.setText(weather.humidity + "%");
				pre_tv.setText(weather.precipitation + "mm");
			} else {
				Toast.makeText(getBaseContext(), "Unable retrieve weather information. Check your internet connection and try again.", Toast.LENGTH_LONG).show();
			}
        }
	}
}
