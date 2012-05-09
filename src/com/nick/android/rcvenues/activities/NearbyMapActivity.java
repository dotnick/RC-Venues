package com.nick.android.rcvenues.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.nick.android.rcflyinglocations.R;
import com.nick.android.rcvenues.MapOverlay;
import com.nick.android.rcvenues.database.DatabaseHandler;

public class NearbyMapActivity extends SherlockMapActivity {

	private MapView mapView;
	private List<Overlay> mapOverLays;
	private Drawable marker;
	private MapOverlay mapOverlay;
	private DatabaseHandler dbHandler;
	private LocationManager locationManager;
	private GeoPoint currentPoint;
	private Location currentLocation = null;
	private MapController mapController;

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
		marker = this.getResources().getDrawable(R.drawable.red_marker);
		mapOverlay = new MapOverlay(marker, mapView);	
		
		
	/*	for(Venue v : nearbyVenues) {
			
			GeoPoint point = new GeoPoint( (int) (v.getLatitude() * 1E6), ((int) (v.getLongitude() * 1E6)));
	        OverlayItem overlayItem = new OverlayItem(point, "", "");
	    	mapOverlay.addOverlay(overlayItem);
	    	
		}
	*/	mapOverLays.add(mapOverlay);
		
		if(currentPoint != null) {
			mapView.getController().animateTo(currentPoint);
			mapView.getController().setZoom(18);
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