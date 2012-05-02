package com.nick.android.rcflyinglocations;

import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "venuesDB";
 
    // Contacts table name
    private static final String TABLE_NAME = "venues";
 
    // Venue Table Columns names
    private static final String KEY_ID = "id";
    private static final String LONGTITUDE= "longtitude";
    private static final String LATITUDE = "latitude";
    private static final String NAME = "name";
    private static final String TYPE = "type";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VENUES_TABLE = "CREATE TABLE venues (id integer primary key autoincrement, name text, longtitude integer, latitude integer, type integer, country text, city text);";
        db.execSQL(CREATE_VENUES_TABLE);
        Log.d("RC Locator", "TABLE CREATED");
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // Create table again
        onCreate(db);
    }
 
    void addVenue(Venue venue) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(NAME, venue.getName()); 
        values.put(LONGTITUDE, venue.getLongtitude());
        values.put(LATITUDE, venue.getLatitude());
        values.put(TYPE, venue.getType());
        values.put(COUNTRY, venue.getCountry());
        values.put(CITY, venue.getCity());
 
        db.insert(TABLE_NAME, null, values);
        db.close(); 
    }
 
    public Vector<Venue> getAllVenues() {
    	
        Vector<Venue> venueList = new Vector<Venue>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 

        if (cursor.moveToFirst()) {
            do {
                Venue venue = new Venue();
                venue.setId(Integer.parseInt(cursor.getString(0)));
                venue.setName(cursor.getString(1));
                venue.setLongtitude(Integer.parseInt(cursor.getString(2)));
                venue.setLatitude(Integer.parseInt(cursor.getString(3)));
                venue.setType(Integer.parseInt(cursor.getString(4)));
                venue.setCountry(cursor.getColumnName(5));
                venue.setCity(cursor.getColumnName(6));
        
                venueList.addElement(venue);
            } while (cursor.moveToNext());
        }
 
        return venueList;
    }
 
    public int getVenueCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        return cursor.getCount();
    }
 
}
