package com.nick.android.rcvenues.activities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.analytics.tracking.android.EasyTracker;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.Venue;
import com.nick.android.rcvenues.database.DatabaseHandler;

public class VenuesActivity extends SherlockActivity {

	private DatabaseHandler dbHandler;
	private EditText et;
	private ArrayList<Venue> venueList;
	private VenueAdapter vAdapter;
	private TextWatcher tw;
	private HashMap<String, Integer> venueID;
	private boolean[] selectedFilters = { true, true, true };
	private ListView venueLv;
	
	private String formURL = "http://rcvenues.zzl.org/";

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.venue_list);
		setProgress(Boolean.FALSE);
		dbHandler = DatabaseHandler.getHelper(getApplicationContext());
		venueList = dbHandler.getAllVenues();
		venueLv = (ListView) findViewById(R.id.venue_lv);

		// Create a venue - Id map to retrieve info about venues from db
		venueID = new HashMap<String, Integer>();
		Collections.sort(venueList, new VenueComparator());
		createVenueMap(venueList);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Venues");

		vAdapter = new VenueAdapter(getBaseContext(), R.layout.row, venueList);
		venueLv.setAdapter(vAdapter);
		venueLv.setOnItemClickListener(listener);
		tw = new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				ArrayList<Venue> filterList = new ArrayList<Venue>();

				if (s.length() == 0) {
					venueLv.setAdapter(vAdapter);
				} else {
					for (Venue v : venueList) {
						String vStr = v.toString().toLowerCase();
						if (vStr.contains(s.toString().toLowerCase())) {
							filterList.add(v);
						}
					}
					VenueAdapter filterAdapter = new VenueAdapter(
							getBaseContext(), R.layout.row, filterList);
					venueLv.setAdapter(filterAdapter);
				}
			}
		};
		registerForContextMenu(venueLv);
		EasyTracker.getInstance().activityStart(this); 
	}

	@Override
	public void onResume() {
		super.onResume();
		EasyTracker.getInstance().activityStop(this); 
	}

	@Override
	public void onPause() {
		super.onPause();
		EasyTracker.getInstance().activityStop(this);
	}

	public void createVenueMap(ArrayList<Venue> venueList) {
		venueID.clear();
		for (int i = 0; i < venueList.size(); i++) {
			venueID.put(venueList.get(i).getName() + " "
					+ venueList.get(i).getAddress(), venueList.get(i).getID());
		}
	}

	private OnItemClickListener listener = new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent toDetails = new Intent(VenuesActivity.this,
					VenueDetailsActivity.class);
			Venue venue = (Venue) parent.getItemAtPosition(position);
			String venueKey = venue.getName() + " " + venue.getAddress();
			toDetails.putExtra("id", venueID.get(venueKey));
			startActivity(toDetails);
			
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, Menu.FIRST, Menu.NONE, "Filter").setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(0, Menu.FIRST + 1, Menu.NONE, "Search")
				.setIcon(R.drawable.btn_action_search)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(0, Menu.FIRST + 2, Menu.NONE, "Sync")
				.setIcon(R.drawable.refresh)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add(0, Menu.FIRST + 3, Menu.NONE, "Add Venue")
		.setIcon(R.drawable.btn_add_venue)
		.setShowAsAction(
				MenuItem.SHOW_AS_ACTION_ALWAYS
						| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return super.onCreateOptionsMenu(menu);
	}

	private void setProgress(boolean status) {
		setSupportProgressBarIndeterminateVisibility(status);
	}

	private void updateFilters() {
		ArrayList<Venue> filteredList = dbHandler
				.getFilteredVenues(selectedFilters);
		vAdapter = new VenueAdapter(getBaseContext(), R.layout.row,
				filteredList);
		venueLv.setAdapter(vAdapter);

	}

	private void showFilterDialog() {
		final CharSequence[] filters = { "Air", "Ground", "Water" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select venue type(s):");
		builder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						updateFilters();
						dialog.cancel();
					}
				});

		builder.setMultiChoiceItems(filters, selectedFilters,
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						selectedFilters[which] = isChecked;

					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void updateVenues() {
		setProgress(Boolean.TRUE);
		new UpdateVenuesTask().execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case Menu.FIRST + 2:
			updateVenues();
			return true;
		case Menu.FIRST:
			showFilterDialog();
			return true;
		case Menu.FIRST + 1:
			item.setActionView(R.layout.collapsible_search);
			et = (EditText) item.getActionView().findViewById(
					R.id.action_search);
			et.addTextChangedListener(tw);
			return true;
		case Menu.FIRST + 3:
				toAddVenueForm();
				return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void toAddVenueForm() {
		Intent toAddVenueForm = new Intent(Intent.ACTION_VIEW);
		toAddVenueForm.setData(Uri.parse(formURL));
		startActivity(toAddVenueForm);
	}
	
	public Long getLastCheckedTime() {
		Activity activity = (Activity) this;
		SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE);
		return pref.getLong("last_checked", 0L);
	}
	
	public void updateLastCheckedTime() {
		Activity activity = (Activity) this;
		Long last_checked = System.currentTimeMillis() / 1000;
		SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong("last_checked", last_checked);
		editor.commit();
	}
	

	private class UpdateVenuesTask extends
			AsyncTask<Void, Void, ArrayList<Venue>> {

		private DatabaseHandler dbHandler1;
		private Context mContext;

		@Override
		protected ArrayList<Venue> doInBackground(Void... params) {
			dbHandler1 = DatabaseHandler.getHelper(getApplicationContext());
			mContext = getBaseContext();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(
								getBaseContext(),
								"No new venues.",
								Toast.LENGTH_LONG).show();
						setProgress(false);
					}
				});
			}
			return null;
		}
	}
}

class VenueAdapter extends ArrayAdapter<Venue> {

	private ArrayList<Venue> venues;
	private Context mContext;
	private DatabaseHandler dbHandler;

	public VenueAdapter(Context context, int textViewResourceId,
			ArrayList<Venue> venues) {
		super(context, textViewResourceId, venues);
		this.venues = venues;
		this.mContext = context;
		dbHandler = new DatabaseHandler(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final Venue venue = venues.get(position);
		View rowView = inflater.inflate(R.layout.row, parent, false);
		TextView vName = (TextView) rowView.findViewById(R.id.toptext);
		TextView vAddress = (TextView) rowView.findViewById(R.id.bottomtext);
		final ImageView iView = (ImageView) rowView.findViewById(R.id.fav_icon);
		vName.setText(venue.getName());
		vAddress.setText(venue.getAddress());
		if (venue.isFavourite()) {
			iView.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.favourite));
		}

		iView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (venue.isFavourite()) {
					venue.setFavourite(false);
					dbHandler.setFavourite(venue.getID(), false);
					iView.setImageDrawable(mContext.getResources().getDrawable(
							R.drawable.not_favourite));
				} else {
					venue.setFavourite(true);
					dbHandler.setFavourite(venue.getID(), true);
					iView.setImageDrawable(mContext.getResources().getDrawable(
							R.drawable.favourite));
				}
			}

		});

		iView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				if (venue.isFavourite()) {
					Toast.makeText(mContext, "Remove venue from favourites ",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(mContext, "Add venue to favourites ",
							Toast.LENGTH_LONG).show();
				}
				return true;
			}
		});

		return rowView;
	}
	
}

class VenueComparator implements Comparator<Venue> {
	public int compare(Venue v1, Venue v2) {
		return v1.getName().compareTo(v2.getName());
	}
}
