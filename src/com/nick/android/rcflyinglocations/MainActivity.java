package com.nick.android.rcflyinglocations;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button searchButton;
	private Button mapButton;
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
		this.mapButton = (Button) this.findViewById(R.id.btn_map);
		this.mapButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent toMap = new Intent(MainActivity.this,
						MapsActivity.class);
				toMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(toMap);

			}
		});
	}

}
