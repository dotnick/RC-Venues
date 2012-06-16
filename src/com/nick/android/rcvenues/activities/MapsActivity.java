package com.nick.android.rcvenues.activities;

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
import com.nick.android.rcvenues.Venue;
import com.nick.android.rcvenues.database.DatabaseHandler;


public class MapsActivity extends SherlockMapActivity {
  
    private MapView mapView;
    private List<Overlay> mapOverLays;
    private Drawable marker;
    private MapOverlay mapOverlay;
    private int venueId;
    private String venueName, venueAddr;
    private DatabaseHandler dbHandler;
    
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
        
        // Fix marker shadow position
        int w = marker.getIntrinsicWidth();
    	int h = marker.getIntrinsicHeight();
    	marker.setBounds(-w / 2, -h, w / 2, 0);
    	
        mapOverlay = new MapOverlay(marker, mapView);

        /* 
         * points[0] -> latitude,
         * points[1] -> longtitude
         */
        double points[] = this.getIntent().getDoubleArrayExtra("points");
    	venueId = this.getIntent().getIntExtra("id", -1);
    	dbHandler = new DatabaseHandler(this);
    	if(venueId != -1) {
    		Venue v = dbHandler.getVenueInfo(venueId);
    		venueName = v.getName();
    		venueAddr = v.getAddress();
    	}
    	GeoPoint point = new GeoPoint((int) (points[0] * 1E6),(int) (points[1] * 1E6));
    	
    	mapOverlay.addOverlay(new OverlayItem(point, venueName, venueAddr));
    	mapOverLays.add(mapOverlay);
    	
        mapView.getController().animateTo(point);
        mapView.getController().setZoom(15);
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