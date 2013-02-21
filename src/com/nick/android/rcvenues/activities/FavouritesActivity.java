package com.nick.android.rcvenues.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.Venue;
import com.nick.android.rcvenues.database.DatabaseHandler;

public class FavouritesActivity extends SherlockActivity {

	private DatabaseHandler dbHandler;
	private ArrayList<Venue> favouriteVenues;
	private VenueAdapter vAdapter;
	private HashMap<String, Integer> venueID;
	private ListView venueLv;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.venue_list);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Favourite Venues");

		dbHandler = DatabaseHandler.getHelper(getApplicationContext());
		favouriteVenues = dbHandler.getFavouriteVenues();
		dbHandler.close();
		venueLv = (ListView) findViewById(R.id.venue_lv);
		
		venueLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent toDetails = new Intent(FavouritesActivity.this,
						VenueDetailsActivity.class);
				Venue venue = (Venue) parent.getItemAtPosition(position);
				String venueKey = venue.getName() + " " + venue.getAddress();
				toDetails.putExtra("id", venueID.get(venueKey));
				startActivity(toDetails);
				
			}
			
		});

		if (favouriteVenues != null) {
			// Create a venue - Id map to retrieve info about venues from db
			venueID = new HashMap<String, Integer>();
			for (int i = 0; i < favouriteVenues.size(); i++) {
				venueID.put(favouriteVenues.get(i).getName() + " "
						+ favouriteVenues.get(i).getAddress(), favouriteVenues
						.get(i).getID());
			}

			vAdapter = new VenueAdapter(this, R.layout.row, favouriteVenues);
			venueLv.setAdapter(vAdapter);
		}
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
