package com.nick.android.rcflyinglocations;

import java.util.List;
import java.util.Vector;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class MapsActivity extends MapActivity {
  
    private MapView mapView;
    private List<Overlay> mapOverLays;
    private Drawable marker;
    private MapOverlay mapOverlay;
    private DatabaseHandler dbHandler;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapOverLays = mapView.getOverlays();
        marker = this.getResources().getDrawable(R.drawable.marker);
        mapOverlay = new MapOverlay(marker);
        dbHandler = new DatabaseHandler(getBaseContext());
        
        //dbHandler.addVenue(new Venue(0, 19240000,-99120000, "Mexico Park", 1, "Mexico City", "Mexico"));
        //dbHandler.addVenue(new Venue(0, 51871404,-3378296, "Caledonian Park", 2, "London", "UK" ));
        Vector<Venue> venues = dbHandler.getAllVenues();
        Log.d("Number of Venues", Integer.toString(venues.size()));
        
        for(int i=0; i<venues.size(); i++){
    	   GeoPoint point = new GeoPoint(venues.elementAt(i).getLongtitude(), venues.elementAt(i).getLatitude());
    	   OverlayItem overlayItem = new OverlayItem(point, "", "");
        
    	   mapOverlay.addOverlay(overlayItem);
    	   mapOverLays.add(mapOverlay);
       }
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}