package com.nick.android.rcflyinglocations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class VenueDetailsActivity extends SherlockActivity {
	
	private TextView venueNameTextView;
	private TextView venueDescriptionTextView;
	private TextView venueCityTextView;
	private TextView venueCountryTextView;
	private Button viewOnMapButton;
	private DatabaseHandler dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setTitle("Venue details");
        
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
	
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	

}
