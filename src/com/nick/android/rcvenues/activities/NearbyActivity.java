package com.nick.android.rcvenues.activities;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.database.DatabaseHandler;

public class NearbyActivity extends SherlockActivity {
	
	private LocationManager locationManager;
	private GeoPoint currentPoint;
	private Location currentLocation = null;
	private ListView lv;
	private String listview_array[];
	private ArrayList<String> array_sort = new ArrayList<String>();
	private int textlength = 0;
	private DatabaseHandler dbHandler;
	private ArrayList<String> nearbyVenues;
	
	public void onCreate(Bundle savedInstanceState) {
		
		//listview_array = dbHandler.getVenueStrings();
	
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.nearby);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setHomeButtonEnabled(true);
	    getSupportActionBar().setTitle("Nearby Venues");
	    
	    getLastLocation();
	    
	    dbHandler = new DatabaseHandler(this);
		dbHandler.getReadableDatabase();
		nearbyVenues = dbHandler.getNearbyVenueNames(5, currentPoint.getLongitudeE6() / 1e6, currentPoint.getLatitudeE6() / 1e6);
		if(nearbyVenues.size() < 1) {
	    	Toast.makeText(this, "No nearby venues found.", Toast.LENGTH_LONG)
			.show();
	    } else {
		lv = (ListView) findViewById(R.id.listView_nearby);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nearbyVenues));
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nearbyVenues);
		lv.setAdapter(arrayAdapter); 
		 
		
		
		lv.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				// Hack to get original venue id
				String venueText = (String) lv.getItemAtPosition(position);
				String [] s = venueText.split("\\.", 2);
				Log.d("ID", s[0]);
				int id = Integer.parseInt(s[0]);
				
				Intent toVenueDetails = new Intent(NearbyActivity.this, VenueDetailsActivity.class);
				toVenueDetails.putExtra("id", id);
				startActivity(toVenueDetails);
			}	
		});
	    }
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
	
	public void getLastLocation() {
		String provider = getBestProvider();
		currentLocation = locationManager.getLastKnownLocation(provider);
		if (currentLocation != null) {
			setCurrentLocation(currentLocation);
		} else {
			Toast.makeText(this, "Location not yet acquired", Toast.LENGTH_LONG)
					.show();
		}
	}


	public String getBestProvider() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		criteria.setAccuracy(Criteria.NO_REQUIREMENT);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		return bestProvider;
	}

	public void setCurrentLocation(Location location) {
		int currLatitude = (int) (location.getLatitude() * 1E6);
		int currLongitude = (int) (location.getLongitude() * 1E6);
		currentPoint = new GeoPoint(currLatitude, currLongitude);

		currentLocation = new Location("");
		currentLocation.setLatitude(currentPoint.getLatitudeE6() / 1e6);
		Log.d("Current lat: ", Float.toString((float) (currentPoint.getLatitudeE6() / 1e6)));
		currentLocation.setLongitude(currentPoint.getLongitudeE6() / 1e6);
		Log.d("Current long: ", Float.toString((float) (currentPoint.getLongitudeE6() / 1e6)));
	}
}