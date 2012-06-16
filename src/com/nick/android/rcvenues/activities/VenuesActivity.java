package com.nick.android.rcvenues.activities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.Venue;
import com.nick.android.rcvenues.venueUpdateRequest;
import com.nick.android.rcvenues.database.DatabaseHandler;

public class VenuesActivity extends SherlockListActivity {
	
	private DatabaseHandler dbHandler;
	private EditText et;
	private ArrayList<Venue> venueList;
	private VenueAdapter vAdapter;
	private TextWatcher tw;
	private HashMap<String, Integer> venueID;
	private MenuItem refreshbtn;

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.venue_list);
		setProgress(Boolean.FALSE);
		dbHandler= DatabaseHandler.getHelper(getApplicationContext());
		venueList = dbHandler.getAllVenues();
		
		// Create a venue - Id map to retrieve info about venues from db
		venueID = new HashMap<String, Integer>();
		
		createVenueMap(venueList);
		
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
	
	public void createVenueMap(ArrayList<Venue> venueList) {
		venueID.clear();
		for (int i=0; i<venueList.size(); i++) {
			venueID.put(venueList.get(i).getName() + " " + venueList.get(i).getAddress(), venueList.get(i).getID());
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,Menu.FIRST+1,Menu.NONE, "Search")
        .setIcon(R.drawable.btn_action_search)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(0,Menu.FIRST+2,Menu.NONE, "Sync")
        .setIcon(R.drawable.refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	public void setProgress(boolean status) {
		setSupportProgressBarIndeterminateVisibility(status);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case Menu.FIRST+2:
            	setProgress(Boolean.TRUE);
            	new UpdateVenuesTask().execute();
            	return true;
            case Menu.FIRST+1:
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
	
	private class UpdateVenuesTask extends AsyncTask<Void, Void, ArrayList<Venue>> {
		
		private Socket socket;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		private DatabaseHandler dbHandler1;
		private Context mContext;
		
		@Override
		protected ArrayList<Venue> doInBackground(Void... params) {
			dbHandler1 = DatabaseHandler.getHelper(getApplicationContext());
			mContext = getBaseContext();
        	
			try {
				socket = new Socket(InetAddress.getByName("50.56.218.223"), 8080);
				socket.setSoTimeout(5000);
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				oos.writeObject(new venueUpdateRequest(dbHandler1.getLastMod()));
				oos.flush();
				
				final ArrayList<Venue> updateVenues = (ArrayList<Venue>) ois.readObject();
				if(updateVenues != null && updateVenues.size() > 0) {
					runOnUiThread(new Runnable() {
						  public void run() {
							  dbHandler1.insertVenues(updateVenues);
								ArrayList<Venue> newList = dbHandler1.getAllVenues();
								VenueAdapter filterAdapter = new VenueAdapter(mContext, R.layout.row, newList);
								setListAdapter(filterAdapter);
								createVenueMap(newList);
								Toast.makeText(mContext, "Venues updated", Toast.LENGTH_LONG).show();
						  }
						});
					
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(mContext, "No new venues", Toast.LENGTH_SHORT).show();
					}
					});
				}
				
			} catch (UnknownHostException e) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext, "Unable to connect to venue server. Try again later.", Toast.LENGTH_LONG).show();
						return;
				}
				});
			} catch (IOException e) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext, "Unable to connect to venue server. Try again later.", Toast.LENGTH_LONG).show();
						return;
				}
				});
			} catch (ClassNotFoundException e) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext, "Unable to connect to venue server. Try again later.", Toast.LENGTH_LONG).show();
						return;
				}
				});
			} finally {
				runOnUiThread(new Runnable() {
					  public void run() {
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
	
