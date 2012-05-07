package com.nick.android.rcflyinglocations;

public class Venue {
	
	private double longitude;
	private double latitude;
	private String name;
	private String description;
	private int type;
	private String address;
	private int id;
	
	
	public Venue(String name, String description, double longitude, double latitude,  int type, String address) {
		
		this.longitude = longitude;
		this.latitude = latitude;
		this.name = name;
		this.type = type;
		this.address = address;
		
	
	}
	
	public Venue() {
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public double getLongitude() {
		return this.longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return this.latitude;
	}
	
	public void setLatitude(double d) {
		this.latitude = d;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return new String(this.id + ". " + this.name + " " + this.address + " ");
	}
}

