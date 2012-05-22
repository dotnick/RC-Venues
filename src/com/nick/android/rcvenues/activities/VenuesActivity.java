package com.nick.android.rcvenues.activities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.database.DatabaseHandler;
import com.nick.android.rcvenues.database.Venue;

public class VenuesActivity extends SherlockListActivity {
	
	private DatabaseHandler dbHandler;
	private EditText et;
	private ArrayList<Venue> venueList;
	private VenueAdapter vAdapter;
	private TextWatcher tw;
	private HashMap<String, Integer> venueID;

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venue_list);
	
		dbHandler = new DatabaseHandler(this);
		dbHandler.openDataBase();
		venueList = dbHandler.getAllVenues();
		dbHandler.close();
		
		// Create a venue - Id map to retrieve info about venues from db
		venueID = new HashMap<String, Integer>();
		
		for (int i=0; i<venueList.size(); i++) {
			venueID.put(venueList.get(i).getName() + " " + venueList.get(i).getAddress(), venueList.get(i).getID());
		}
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setHomeButtonEnabled(true);
	    getSupportActionBar().setTitle("Venues");
	        
		vAdapter = new VenueAdapter(this, R.layout.row, venueList);
		setListAdapter(vAdapter);
		
		tw = new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				ArrayList<Venue> filterList = new ArrayList<Venue>();
				String st = et.getText().toString().toLowerCase();
				
				if(s.length()==0){
					setListAdapter(vAdapter);
				} else {
					for (Venue v : venueList) { 
						ArrayList<Character> venueCharList = new ArrayList<Character>();
						ArrayList<Character> searchCharList = new ArrayList<Character>();
						String vStr = v.toString().toLowerCase();
						for(int i=0; i<vStr.length(); i++){
							venueCharList.add(vStr.charAt(i));
						}
						for(int i=0; i<st.length(); i++){
							searchCharList.add(st.charAt(i));
						}
			            if(venueCharList.containsAll(searchCharList)){
			            	filterList.add(v);
			            }
			        }		
					VenueAdapter filterAdapter = new VenueAdapter(getBaseContext(), R.layout.row, filterList);
					setListAdapter(filterAdapter);
				}
			}
		};

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,Menu.FIRST,Menu.NONE, "Search")
        .setIcon(R.drawable.btn_action_search)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(0,Menu.FIRST+1,Menu.NONE, "Sync")
        .setIcon(R.drawable.refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.refresh_btn:
                new UpdateVenuesTask().execute(this);
                return true;
            case Menu.FIRST:
            	item.setActionView(R.layout.collapsible_search);
            	et = (EditText) item.getActionView().findViewById(R.id.action_search);
            	et.addTextChangedListener(tw);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent toDetails = new Intent(VenuesActivity.this, VenueDetailsActivity.class);
		Venue venue = (Venue) l.getItemAtPosition(position);
		String venueKey = venue.getName() + " " + venue.getAddress();
		toDetails.putExtra("id", venueID.get(venueKey));
		startActivity(toDetails);
	}
}


class UpdateVenuesTask extends AsyncTask<Context, Integer, Long> {

	@Override
	protected Long doInBackground(Context... params) {
		String TEMP_FILENAME = "db.temp";
		DatabaseHandler dbHandler = new DatabaseHandler(params[0]);
		byte[] result = dbHandler.checkForUpdate();
		if(result != null) {
			try {
				FileOutputStream fos = params[0].openFileOutput(TEMP_FILENAME, 
						Context.MODE_PRIVATE);
				fos.write(result);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

}

class VenueAdapter extends ArrayAdapter<Venue> {

    private ArrayList<Venue> venues;
    private Context mContext;
    private DatabaseHandler dbHandler;
    
    public VenueAdapter(Context context, int textViewResourceId, ArrayList<Venue> venues) {
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
		vName.setText(venue.getName());;
		vAddress.setText(venue.getAddress());
		if(venue.isFavourite()) {
			iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.favourite));
		}
		
		iView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(venue.isFavourite()) {
					venue.setFavourite(false);
					dbHandler.setFavourite(venue.getID(), false);
					iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.not_favourite));
				} else {
					venue.setFavourite(true);
					dbHandler.setFavourite(venue.getID(), true);
					iView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.favourite));
				}
			}
			
		});
		
		iView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if(venue.isFavourite()) {
					Toast.makeText(mContext, "Remove venue from favourites ", Toast.LENGTH_LONG).show();
				} else{
					Toast.makeText(mContext, "Add venue to favourites ", Toast.LENGTH_LONG).show();
				}
				return true;
			}
		});
		
		return rowView;
	}
}
	
