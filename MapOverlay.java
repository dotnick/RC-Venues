package com.nick.android.rcflyinglocations;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

public class MapOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverLays = new ArrayList<OverlayItem>();
	private Context mContext;

	public MapOverlay(Drawable marker, MapView mapView) {
		super(marker, mapView);

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
	    Toast.makeText(mContext, "Overlay Item " + index + " tapped!",
	            Toast.LENGTH_LONG).show();
	    return true;
	}

}
