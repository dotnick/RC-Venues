package com.nick.android.rcflyinglocations;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends SherlockActivity {
	private DatabaseHandler dbHandler;
	private ListView lv;
	private EditText et;
	private String listview_array[];
	private ArrayList<String> array_sort = new ArrayList<String>();
	int textlength = 0;

	public void onCreate(Bundle savedInstanceState) {
		dbHandler = new DatabaseHandler(this);
		ArrayList<String> venues =  dbHandler.getAllVenueNames();
		listview_array = venues.toArray(new String[venues.size()]);
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.search_list);

		lv = (ListView) findViewById(R.id.ListView01);
		et = (EditText) findViewById(R.id.EditText01);
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listview_array));
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, venues);
		lv.setAdapter(arrayAdapter); 
		

		et.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				// Abstract Method of TextWatcher Interface.
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// Abstract Method of TextWatcher Interface.
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				textlength = et.getText().length();
				array_sort.clear();
				for (int i = 0; i < listview_array.length; i++) {
					if (textlength <= listview_array[i].length()) {
						if (et.getText()
								.toString()
								.equalsIgnoreCase(
										(String) listview_array[i].subSequence(
												0, textlength))) {
							array_sort.add(listview_array[i]);
						}
					}
				}
				lv.setAdapter(new ArrayAdapter<String>(SearchActivity.this,
						android.R.layout.simple_list_item_1, array_sort));
			}
		});
		
		lv.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String venueName = (String) lv.getItemAtPosition(position);
				
				Intent toVenueDetails = new Intent(SearchActivity.this, VenueDetailsActivity.class);
				toVenueDetails.putExtra("venueName", venueName);
				startActivity(toVenueDetails);
			
			}

		
			
		});
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}