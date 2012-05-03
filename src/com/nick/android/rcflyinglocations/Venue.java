package com.nick.android.rcflyinglocations;

public class Venue {
	
	private int longitude;
	private int latitude;
	private String name;
	private String description;
	private int type;
	private String country;
	private String city;
	
	
	public Venue(int longitude, int latitude, String name, String description, int type, String city, String country) {
		
		this.longitude = longitude;
		this.latitude = latitude;
		this.name = name;
		this.type = type;
		this.country = country;
		this.city = city;
	
	}
	
	public Venue() {
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getlongitude() {
		return this.longitude;
	}
	
	public void setlongitude(int longitude) {
		this.longitude = longitude;
	}
	
	public int getLatitude() {
		return this.latitude;
	}
	
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}

