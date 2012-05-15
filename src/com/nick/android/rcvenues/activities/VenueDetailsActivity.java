package com.nick.android.rcvenues.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.database.DatabaseHandler;
import com.nick.android.rcvenues.database.Venue;

public class VenueDetailsActivity extends SherlockActivity {
	
	private TextView venueNameTextView;
	private TextView venueDescriptionTextView;
	private TextView venueAddressTextView;
	private ImageView viewOnMapButton;
	private DatabaseHandler dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        
		setContentView(R.layout.venue_details);
		
		 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	     getSupportActionBar().setHomeButtonEnabled(true);
	     getSupportActionBar().setTitle("Venue details");
	        
		int venueId = this.getIntent().getIntExtra("id", 0);
		
		venueNameTextView = (TextView) findViewById(R.id.venueName);
		venueAddressTextView = (TextView) findViewById(R.id.venueAddressTextView);
		venueDescriptionTextView = (TextView) findViewById(R.id.venueDescription);
		viewOnMapButton = (ImageView) findViewById(R.id.btn_map);
		
		
		
		dbHandler = new DatabaseHandler(this);
		
		final Venue venue = dbHandler.getVenueInfo(venueId);
		
		venueNameTextView.setText(venue.getName());
		venueDescriptionTextView.setText(venue.getDescription());
		
		viewOnMapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toMap = new Intent(VenueDetailsActivity.this, MapsActivity.class);
				Log.d("Venue location: " , venue.getLatitude() + " " + venue.getLongitude());
				toMap.putExtra("points", new double[] { venue.getLatitude(), venue.getLongitude() });
				startActivity(toMap);
				
			}
			
		});
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
