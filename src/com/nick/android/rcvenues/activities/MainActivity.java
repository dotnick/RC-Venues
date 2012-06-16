package com.nick.android.rcvenues.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.database.DatabaseHandler;

public class MainActivity extends SherlockActivity {

	private DatabaseHandler dbHandler;
	private TextView venuesTopTv, venuesBottomTv, favouritesTopTv,
			favouritesBottomTv, weatherTopTv, weatherBottomTv;
	private View venuesTr, favouritesTr, weatherTr;
	private Button venuesBtn, favouritesBtn, weatherBtn;
	private Typeface robotoThin;
	private Typeface robotoMed;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		robotoThin = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
		robotoMed = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");
		dbHandler = new DatabaseHandler(this);
		dbHandler.createdatabase();
		this.setContentView(R.layout.main);

		venuesBtn = (Button) findViewById(R.id.venues_btn);
		venuesTopTv = (TextView) findViewById(R.id.venues_top_tv);
		venuesBottomTv = (TextView) findViewById(R.id.venues_bottom_tv);
		venuesTr = (View) findViewById(R.id.venues_tr);

		favouritesBtn = (Button) findViewById(R.id.favourites_btn);
		favouritesTopTv = (TextView) findViewById(R.id.favourites_top_tv);
		favouritesBottomTv = (TextView) findViewById(R.id.favourites_bottom_tv);
		favouritesTr = (View) findViewById(R.id.favourites_tr);

		weatherBtn = (Button) findViewById(R.id.weather_btn);
		weatherTopTv = (TextView) findViewById(R.id.weather_top_tv);
		weatherBottomTv = (TextView) findViewById(R.id.weather_bottom_tv);
		weatherTr = (View) findViewById(R.id.weather_tr);

		venuesTopTv.setTypeface(robotoThin);
		favouritesTopTv.setTypeface(robotoThin);
		weatherTopTv.setTypeface(robotoThin);

		venuesBottomTv.setTypeface(robotoMed);
		favouritesBottomTv.setTypeface(robotoMed);
		weatherBottomTv.setTypeface(robotoMed);

		setUpListeners();
	}



	/*
	 * Listeners for touch animations
	 */
	public void setUpListeners() {
		
		/*
		 * Venues Button
		 */
		venuesBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					venuesTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					venuesTr.setBackgroundResource(R.color.not_clicked);
					toVenues();
				}
				return false;
			}
		});

		venuesTopTv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					venuesTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					venuesTr.setBackgroundResource(R.color.not_clicked);
					toVenues();
				}
				return false;
			}
		});

		venuesBottomTv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					venuesTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					venuesTr.setBackgroundResource(R.color.not_clicked);
					toVenues();
				}
				return false;
			}
		});

		venuesTr.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					venuesTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					venuesTr.setBackgroundResource(R.color.not_clicked);
					toVenues();
				}
				return false;
			}
		});

		/*
		 * Favourites button
		 */
		
		favouritesBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					favouritesTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					favouritesTr.setBackgroundResource(R.color.not_clicked);
					toFavourites();
				}
				return false;
			}
		});

		favouritesTopTv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					favouritesTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					favouritesTr.setBackgroundResource(R.color.not_clicked);
					toFavourites();
				}
				return false;
			}
		});

		favouritesBottomTv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					favouritesTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					favouritesTr.setBackgroundResource(R.color.not_clicked);
					toFavourites();
				}
				return false;
			}
		});

		favouritesTr.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					favouritesTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					favouritesTr.setBackgroundResource(R.color.not_clicked);
					toFavourites();
				}
				return false;
			}
		});
		
		/*
		 * Weather Button
		 */
		weatherBtn.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					weatherTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					weatherTr.setBackgroundResource(R.color.not_clicked);
					toWeather();
				}
				return false;
			}
		});

		weatherTopTv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					weatherTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					weatherTr.setBackgroundResource(R.color.not_clicked);
					toWeather();
				}
				return false;
			}
		});

		weatherBottomTv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					weatherTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					weatherTr.setBackgroundResource(R.color.not_clicked);
					toWeather();
				}
				return false;
			}
		});

		weatherTr.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					weatherTr.setBackgroundResource(R.color.clicked);
					return true;
				case MotionEvent.ACTION_UP:
					weatherTr.setBackgroundResource(R.color.not_clicked);
					toWeather();
				}
				return false;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		venuesTr.setBackgroundResource(R.color.not_clicked);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.about, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void toVenues() {
		Intent toVenues = new Intent(MainActivity.this, VenuesActivity.class);
		startActivity(toVenues);
	}

	public void toFavourites() {
		Intent toFavourites = new Intent(MainActivity.this,
				FavouritesActivity.class);
		startActivity(toFavourites);
	}

	public void toWeather() {
		Intent toWeather = new Intent(MainActivity.this,
				WeatherVenueSelect.class);
		startActivity(toWeather);
	}
}
