package com.nick.android.rcvenues;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class MapOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverLays = new ArrayList<OverlayItem>();
	public MapOverlay(Drawable marker, MapView mapView) {
		super(boundCenter(marker), mapView);
		boundCenter(marker);
		mapView.getContext();

	}

	public void addOverlay(OverlayItem overlay) {
		mOverLays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverLays.get(i);
	}

	@Override
	public int size() {
		return mOverLays.size();
	}
	
	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
	    return true;
	}

}
