package com.nick.android.rcflyinglocations;

public class Venue {
	
	private double longtitude;
	private double latitute;
	private String name;
	//private String description;
	private int type;
	
	
	public Venue(double longtitude, double latitude, String name, int type) {
		
		this.longtitude = longtitude;
		this.latitute = latitude;
		this.name = name;
		this.type = type;
	
	}
	
	public String getName() {
		return this.name;
	}

	public Double getLongtitude() {
		return this.longtitude;
	}
	
	public Double getLatitude() {
		return this.latitute;
	}
	
	public int getType() {
		return this.type;
	}
}

