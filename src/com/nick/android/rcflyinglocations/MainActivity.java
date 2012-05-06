package com.nick.android.rcflyinglocations;

import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockActivity;

public class MainActivity extends SherlockActivity {

	private Button searchButton;
	private Button nearbyButton;
	private DatabaseHandler dbHandler;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHandler = new DatabaseHandler(this);
		try {
			dbHandler.createDataBase();
			dbHandler.openDataBase();
			dbHandler.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		this.setContentView(R.layout.fragment_layout);
		this.searchButton = (Button) this.findViewById(R.id.btn_search);
		this.searchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toSearch = new Intent(MainActivity.this,
						SearchActivity.class);
				toSearch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(toSearch);

			}
		});
		
		this.nearbyButton = (Button) findViewById(R.id.btn_nearby);
		this.nearbyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent toNearby = new Intent(MainActivity.this, NearbyActivity.class);
				startActivity(toNearby);
				
			}
			
		});
		
		
	}
}
