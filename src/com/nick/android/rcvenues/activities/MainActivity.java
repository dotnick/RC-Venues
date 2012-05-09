package com.nick.android.rcvenues.activities;

import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.database.DatabaseHandler;

public class MainActivity extends SherlockActivity {

	private Button searchButton;
	private Button nearbyButton;
	private DatabaseHandler dbHandler;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHandler = new DatabaseHandler(this);
		try {
			dbHandler.createDataBase();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		this.setContentView(R.layout.main);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.about, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
