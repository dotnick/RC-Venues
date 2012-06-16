package com.nick.android.rcvenues;

import java.io.Serializable;

public class venueUpdateRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private long lastMod;
	
	public venueUpdateRequest(long lastMod) {
		this.lastMod = lastMod;
	}
	
	public long getLastMod() {
		return this.lastMod;
	}
}
