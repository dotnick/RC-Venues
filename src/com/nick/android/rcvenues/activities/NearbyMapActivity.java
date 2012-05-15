package com.nick.android.rcvenues.activities;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.nick.android.rcvenues.MapOverlay;
import com.nick.android.rcvenues.R;
import com.nick.android.rcvenues.database.Venue;

public class NearbyMapActivity extends SherlockMapActivity {

	private MapView mapView;
	private List<Overlay> mapOverLays;
	private Drawable nearbyMarker;
	private Drawable currentLocationMarker;
	private MapOverlay mapOverlay;
	private MapOverlay currentLocationOverlay;
	private GeoPoint currentPoint;
	private double currentLat;
	private double currentLong;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.map);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("Map");

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapOverLays = mapView.getOverlays();
		nearbyMarker = this.getResources().getDrawable(R.drawable.red_marker);
		currentLocationMarker = this.getResources().getDrawable(R.drawable.marker);
		mapOverlay = new MapOverlay(nearbyMarker, mapView);
		currentLocationOverlay = new MapOverlay(currentLocationMarker, mapView);
		ArrayList<Venue> nearbyVenues = this.getIntent().getParcelableArrayListExtra("nearbyVenues");
		
		currentLat = this.getIntent().getDoubleExtra("currentLat", 0.0);
		currentLong = this.getIntent().getDoubleExtra("currentLong", 0.0);
		
		currentPoint = new GeoPoint( (int) (currentLat * 1E6), ((int) (currentLong * 1E6)));
		OverlayItem currentLocOverlayItem = new OverlayItem(currentPoint, "", "");
		currentLocationOverlay.addOverlay(currentLocOverlayItem);
		
		for(Venue v : nearbyVenues) {
			
			GeoPoint point = new GeoPoint( (int) (v.getLatitude() * 1E6), ((int) (v.getLongitude() * 1E6)));
	        OverlayItem overlayItem = new OverlayItem(point, v.getName(), v.getDescription());
	    	mapOverlay.addOverlay(overlayItem);
	    	
	    	mapOverLays.add(currentLocationOverlay);
			mapOverLays.add(mapOverlay);
		}
		
		if(currentPoint != null) {
			mapView.getController().animateTo(currentPoint);
			mapView.getController().setZoom(13);
		}
   
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}