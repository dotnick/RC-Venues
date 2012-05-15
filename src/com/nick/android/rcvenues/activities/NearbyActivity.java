package com.nick.android.rcvenues.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.database.DatabaseHandler;
import com.nick.android.rcvenues.database.Venue;

public class NearbyActivity extends SherlockActivity {
	
	private LocationManager locationManager;
	private Location currentLocation = null;
	private ListView lv;
	private ArrayList<String> nearbyVenueNames = new ArrayList<String>();
	private DatabaseHandler dbHandler;
	private ArrayList<Venue> nearbyVenues;
	private LocationListener locationListener;
	private PendingIntent singleUpatePI;
	private Criteria criteria;
	
	private static final String SINGLE_LOCATION_UPDATE_ACTION = "com.nick.android.rcvenues.SINGLE_LOCATION_UPDATE_ACTION";
	
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.nearby);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setHomeButtonEnabled(true);
	    getSupportActionBar().setTitle("Nearby Venues");
	    
	    // Get location with low accuracy
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_LOW);
	    Intent updateIntent = new Intent(SINGLE_LOCATION_UPDATE_ACTION);  
	    singleUpatePI = PendingIntent.getBroadcast(this, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    currentLocation = getLastBestLocation(5000, 5000);
	    
	    // Get nearby venues based on current location
	    dbHandler = new DatabaseHandler(this);
		dbHandler.getReadableDatabase();
		if(currentLocation != null) {
			nearbyVenues = dbHandler.getNearbyVenueNames(5, currentLocation.getLongitude(), currentLocation.getLatitude());
			
			if(nearbyVenues.size() < 1) {
				((TextView) findViewById(R.id.no_nearby_venues_tv)).setText(getResources().getString(R.string.no_nearby_venues));
		    } else {
		    	for(Venue v : nearbyVenues) {
		    		nearbyVenueNames.add(v.toString());
		    	}
		    	
		    	// Create listview on nearby venues
		    	lv = (ListView) findViewById(R.id.listView_nearby);
		    	lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nearbyVenueNames));	
			
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
		} else {
			Toast.makeText(this, "Unable to get current location", Toast.LENGTH_LONG)
			.show();
		}
	}
		
	
	private void viewNearbyOnMap() {
		Intent toNearbyMap = new Intent(NearbyActivity.this, NearbyMapActivity.class);
		toNearbyMap.putParcelableArrayListExtra("nearbyVenues", nearbyVenues);
		toNearbyMap.putExtra("currentLat", currentLocation.getLatitude());
		toNearbyMap.putExtra("currentLong", currentLocation.getLongitude());
		startActivity(toNearbyMap);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	finish();
                return true;
            case Menu.FIRST:
            	viewNearbyOnMap();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	public Location getLastBestLocation(int minDistance, long minTime) {
	    Location bestResult = null;
	    float bestAccuracy = Float.MAX_VALUE;
	    long bestTime = Long.MIN_VALUE;
	    
	    // Iterate through all the providers on the system, keeping
	    // note of the most accurate result within the acceptable time limit.
	    // If no result is found within maxTime, return the newest Location.
	    List<String> matchingProviders = locationManager.getAllProviders();
	    for (String provider: matchingProviders) {
	      Location location = locationManager.getLastKnownLocation(provider);
	      if (location != null) {
	        float accuracy = location.getAccuracy();
	        long time = location.getTime();
	        
	        if ((time > minTime && accuracy < bestAccuracy)) {
	          bestResult = location;
	          bestAccuracy = accuracy;
	          bestTime = time;
	        }
	        else if (time < minTime && bestAccuracy == Float.MAX_VALUE && time > bestTime) {
	          bestResult = location;
	          bestTime = time;
	        }
	      }
	    }
	    
	    // If the best result is beyond the allowed time limit, or the accuracy of the
	    // best result is wider than the acceptable maximum distance, request a single update.
	    // This check simply implements the same conditions we set when requesting regular
	    // location updates every [minTime] and [minDistance]. 
	    if (locationListener != null && (bestTime < minTime || bestAccuracy > minDistance)) { 
	      IntentFilter locIntentFilter = new IntentFilter(SINGLE_LOCATION_UPDATE_ACTION);
	      this.registerReceiver(singleUpdateReceiver, locIntentFilter);      
	      locationManager.requestSingleUpdate(criteria, singleUpatePI);
	    }
	    
	    return bestResult;
	  }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		if(nearbyVenues.size()>1) {
			menu.add(0,Menu.FIRST,Menu.NONE, "View all on map")
			.setIcon(R.drawable.btn_show_all)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	 protected BroadcastReceiver singleUpdateReceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		      context.unregisterReceiver(singleUpdateReceiver);
		      
		      String key = LocationManager.KEY_LOCATION_CHANGED;
		      Location location = (Location)intent.getExtras().get(key);
		      
		      if (locationListener != null && location != null)
		        locationListener.onLocationChanged(location);
		      
		      locationManager.removeUpdates(singleUpatePI);
		    }
		  };
		  
}