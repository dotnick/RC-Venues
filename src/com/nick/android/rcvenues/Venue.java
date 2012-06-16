package com.nick.android.rcvenues;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Venue implements Parcelable,Serializable {

	private static final long serialVersionUID = 1L;
	private double longitude;
	private double latitude;
	private String name;
	private String description;
	private int type;
	private String address;
	private int id;
	private boolean favourite;

	public Venue(String name, String description, double longitude, double latitude,  int type, String address, boolean favourite) {

		this.name = name;
		this.description = description;
		this.longitude = longitude;
		this.latitude = latitude;
		this.type = type;
		this.address = address;
		this.favourite = favourite;

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

	public int getID() {
		return this.id;
	}

	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}

	public boolean isFavourite() {
		return this.favourite;
	}

	@Override
	public String toString() {
		return new String(this.id + ". " + this.name + " " + this.address + " ");
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeDouble(this.latitude);
		dest.writeDouble(this.longitude);
		dest.writeInt(this.type);
		dest.writeString(this.address);

	}

	public static final Parcelable.Creator<Venue> CREATOR = new Parcelable.Creator<Venue>() {
		public Venue createFromParcel(Parcel in) {
			return new Venue(in);
		}

		public Venue[] newArray(int size) {
			return new Venue[size];
		}
	};

	private Venue(Parcel in) {
       this.name = in.readString();
       this.description = in.readString();
       this.latitude = in.readDouble();
       this.longitude = in.readDouble();
       this.type = in.readInt();
       this.address = in.readString();
    }
}