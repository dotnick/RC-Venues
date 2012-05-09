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


public class MapsActivity extends SherlockMapActivity {
  
    private MapView mapView;
    private List<Overlay> mapOverLays;
    private Drawable marker;
    private MapOverlay mapOverlay;
    
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

        // points[0] -> latitude,
        // points[1] -> longtitude
        double points[] = this.getIntent().getDoubleArrayExtra("points");
       
    	GeoPoint point = new GeoPoint((int) (points[0] * 1E6),(int) (points[1] * 1E6));
       
    	mapOverlay.addOverlay(new OverlayItem(point, "test", "test"));
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