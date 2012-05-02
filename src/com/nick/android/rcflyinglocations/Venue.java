package com.nick.android.rcflyinglocations;

public class Venue {
	
	private int id;
	private int longtitude;
	private int latitude;
	private String name;
	//private String description;
	private int type;
	private String country;
	private String city;
	
	
	public Venue(int id, int longtitude, int latitude, String name, int type, String city, String country) {
		
		this.id = id;
		this.longtitude = longtitude;
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

	public int getLongtitude() {
		return this.longtitude;
	}
	
	public void setLongtitude(int longtitude) {
		this.longtitude = longtitude;
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
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
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
}

