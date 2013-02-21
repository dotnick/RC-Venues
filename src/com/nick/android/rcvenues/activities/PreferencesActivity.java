package com.nick.android.rcvenues.activities;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nick.android.rcvenues.R;

import android.os.Bundle;

public class PreferencesActivity extends SherlockPreferenceActivity {
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("Settings");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.layout.preferences);
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