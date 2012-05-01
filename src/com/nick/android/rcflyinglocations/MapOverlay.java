package com.nick.android.rcflyinglocations;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapOverlay extends ItemizedOverlay {
	
	private ArrayList<OverlayItem> mOverLays = new ArrayList<OverlayItem>();
	

	public MapOverlay(Drawable arg0) {
		super(boundCenterBottom(arg0));
	
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

}
