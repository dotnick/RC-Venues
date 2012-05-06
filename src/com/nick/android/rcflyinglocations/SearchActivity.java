package com.nick.android.rcflyinglocations;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class SearchActivity extends SherlockActivity {
	private DatabaseHandler dbHandler;
	private ListView lv;
	private EditText et;
	private String listview_array[];
	private ArrayList<String> array_sort = new ArrayList<String>();
	int textlength = 0;

	public void onCreate(Bundle savedInstanceState) {
		dbHandler = new DatabaseHandler(this);
		listview_array = dbHandler.getVenueStrings();
	
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.search_list);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setHomeButtonEnabled(true);
	    getSupportActionBar().setTitle("Search for venues");
	        
		lv = (ListView) findViewById(R.id.ListView01);
		et = (EditText) findViewById(R.id.EditText01);
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listview_array));
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listview_array);
		lv.setAdapter(arrayAdapter); 
		
		// Remove focus from search edittext
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	
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
				String st = et.getText().toString();
				for (int i = 0; i < listview_array.length; i++) {
		            if(s.length() <= listview_array[i].length()) {
		                String[] wordArray = listview_array[i].split(" ");
		                for (int j = 0; j < wordArray.length; j++){
		                    if(wordArray[j].toLowerCase().contains(st.toLowerCase())){
		                    	array_sort.add(listview_array[i]);
		                        break; 
		                    }
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
				
				// Hack to get original venue id
				String venueText = (String) lv.getItemAtPosition(position);
				String [] s = venueText.split("\\.", 2);
				Log.d("ID", s[0]);
				int id = Integer.parseInt(s[0]);
				
				Intent toVenueDetails = new Intent(SearchActivity.this, VenueDetailsActivity.class);
				toVenueDetails.putExtra("id", id);
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