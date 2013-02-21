package com.nick.android.rcvenues.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.Venue;
import com.nick.android.rcvenues.database.DatabaseHandler;

public class WeatherVenueSelect extends SherlockActivity implements
		ActionBar.TabListener {
	private ActionBar.Tab allVenuesTab;
	private ActionBar.Tab favouriteVenuesTab;
	private DatabaseHandler dbHandler = DatabaseHandler.getHelper(this);
	private HashMap<String, Integer> venueIdMap;
	private boolean inFavourites;
	private ListView venues_lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Select venue to check weather");

		setContentView(R.layout.weather_venue_select);
		venues_lv = (ListView) findViewById(R.id.venue_lv);
		venues_lv.setOnItemClickListener(listener);
		
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		allVenuesTab = getSupportActionBar().newTab();
		favouriteVenuesTab = getSupportActionBar().newTab();

		allVenuesTab.setText("All venues");
		favouriteVenuesTab.setText("Favourites");

		allVenuesTab.setTabListener(this);
		favouriteVenuesTab.setTabListener(this);

		getSupportActionBar().addTab(allVenuesTab);
		getSupportActionBar().addTab(favouriteVenuesTab);

		venueIdMap = new HashMap<String, Integer>();	

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void createVenueMap(ArrayList<Venue> venueList) {
		venueIdMap.clear();
		for (int i = 0; i < venueList.size(); i++) {
			venueIdMap.put(venueList.get(i).getName() + " "
					+ venueList.get(i).getAddress(), venueList.get(i).getID());
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction transaction) {
	}
	
	private OnItemClickListener listener = new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> l, View v, int position,
				long id) {
	    	Intent toWeather = new Intent(WeatherVenueSelect.this,
					WeatherActivity.class);
			if (inFavourites) {
				Venue venue = (Venue) l.getItemAtPosition(position);
				String venueKey = venue.getName() + " " + venue.getAddress();
				toWeather.putExtra("id", venueIdMap.get(venueKey));
				Log.d("venue id", Integer.toString(venueIdMap.get(venueKey)));
			} else {
				Log.d("venue id", Integer.toString(position + 1));
				toWeather.putExtra("id", position + 1);
			}
			startActivity(toWeather);  	
	    }
	    
	};

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {
		if(venues_lv != null) {
			if (tab.equals(allVenuesTab)) {
				inFavourites = false;
				ArrayList<Venue> venueList = dbHandler.getAllVenues();
				venues_lv.setAdapter(new VenueAdapter(this, R.layout.row, venueList));
			} else {
				inFavourites = true;
				ArrayList<Venue> venueList = dbHandler.getFavouriteVenues();
				createVenueMap(venueList);
				venues_lv.setAdapter(new VenueAdapter(this, R.layout.row, venueList));
			}
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
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
}