package com.nick.android.rcflyinglocations;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class VenueDetailsActivity extends Activity {
	
	private TextView venueNameTextView;
	private TextView venueDescriptionTextView;
	private TextView venueCityTextView;
	private TextView venueCountryTextView;
	private Button viewOnMapButton;
	DatabaseHandler dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		String venueName = this.getIntent().getStringExtra("venueName");
		
		venueNameTextView = (TextView) findViewById(R.id.venueName);
		venueCityTextView = (TextView) findViewById(R.id.venueCityTextView);
		venueCountryTextView = (TextView) findViewById(R.id.venueCountryTextView);
		venueDescriptionTextView = (TextView) findViewById(R.id.venueDescription);
		viewOnMapButton = (Button) findViewById(R.id.viewOnMapButton);
		
		dbHandler = new DatabaseHandler(this);
		
		Venue venue = dbHandler.getVenueInfo(venueName);
		
		venueNameTextView.setText(venue.getName());
		venueCityTextView.setText(venue.getCity());
		venueCountryTextView.setText(venue.getCountry());
		venueDescriptionTextView.setText(venue.getDescription());
	}
	
	

}
