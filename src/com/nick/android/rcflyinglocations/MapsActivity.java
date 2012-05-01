package com.nick.android.rcflyinglocations;

import java.util.List;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class MapsActivity extends MapActivity {
  
    private LinearLayout linearLayout;
    private MapView mapView;
    private List<Overlay> mapOverLays;
    private Drawable marker;
    private MapOverlay mapOverlay;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapOverLays = mapView.getOverlays();
        marker = this.getResources().getDrawable(R.drawable.marker);
        mapOverlay = new MapOverlay(marker);
        
        GeoPoint point = new GeoPoint(19240000,-99120000);
        OverlayItem overlayItem = new OverlayItem(point, "", "");
        
        mapOverlay.addOverlay(overlayItem);
        mapOverLays.add(mapOverlay);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}