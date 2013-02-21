package com.nick.android.rcvenues.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.Venue;
import com.nick.android.rcvenues.database.DatabaseHandler;

public class VenueDetailsActivity extends SherlockActivity {
	
	private TextView venueNameTextView;
	private TextView venueDescriptionTextView;
	private TextView venueAddressTextView;
	private ImageView viewOnMapButton, viewWeatherButton;
	private DatabaseHandler dbHandler;
	private int venueId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        
		setContentView(R.layout.venue_details);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setHomeButtonEnabled(true);
	    getSupportActionBar().setTitle("Venue details");
	        
		venueId = this.getIntent().getIntExtra("id", 0);
		
		venueNameTextView = (TextView) findViewById(R.id.venueName);
		venueAddressTextView = (TextView) findViewById(R.id.venueAddressTextView);
		venueDescriptionTextView = (TextView) findViewById(R.id.venueDescription);
		venueAddressTextView = (TextView) findViewById(R.id.venueAddressTextView);
		viewOnMapButton = (ImageView) findViewById(R.id.btn_map);
		viewWeatherButton = (ImageView) findViewById(R.id.btn_weather);
		
		dbHandler = DatabaseHandler.getHelper(getApplicationContext());
		
		final Venue venue = dbHandler.getVenueInfo(venueId);
		venueNameTextView.setText(venue.getName());
		venueDescriptionTextView.setText(venue.getDescription());
		venueDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());
		venueAddressTextView.setText(venue.getAddress());
		
	
		viewOnMapButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toMap = new Intent(VenueDetailsActivity.this, MapsActivity.class);
				toMap.putExtra("id", venueId);
				toMap.putExtra("points", new double[] { venue.getLatitude(), venue.getLongitude() });
				startActivity(toMap);		
			}
		});
		
		viewWeatherButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toWeather = new Intent(VenueDetailsActivity.this, WeatherActivity.class);
				toWeather.putExtra("id", venueId);
				startActivity(toWeather);		
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	

}
