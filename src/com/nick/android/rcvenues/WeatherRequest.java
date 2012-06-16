package com.nick.android.rcvenues;

import java.io.Serializable;

public class WeatherRequest implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private double lat;
	private double longi;
	
	public WeatherRequest(double lat, double longi) {
		this.lat = lat;
		this.longi = longi;
	}
	
	public double getLat() {
		return this.lat;
	}
	
	public double getLong() {
		return this.longi;
	}
}
