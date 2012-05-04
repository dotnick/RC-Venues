package com.nick.android.rcflyinglocations;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class MapsActivity extends SherlockMapActivity {
  
    private MapView mapView;
    private List<Overlay> mapOverLays;
    private Drawable marker;
    private MapOverlay mapOverlay;
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
        marker = this.getResources().getDrawable(R.drawable.marker);
        mapOverlay = new MapOverlay(marker);
        dbHandler = new DatabaseHandler(getBaseContext());
        
        
        dbHandler.openDataBase();
        
        
        ArrayList<Venue> venues = dbHandler.getAllVenues();
        Log.d("Number of Venues", Integer.toString(venues.size()));
        dbHandler.close();
        
        for(int i=0; i<venues.size(); i++){
    	   GeoPoint point = new GeoPoint(venues.get(i).getlongitude(),venues.get(i).getLatitude());
    	   OverlayItem overlayItem = new OverlayItem(point, "", "");
        
    	   mapOverlay.addOverlay(overlayItem);
    	   mapOverLays.add(mapOverlay);
       }
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
    
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}