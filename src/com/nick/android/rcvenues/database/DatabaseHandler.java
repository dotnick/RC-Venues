package com.nick.android.rcvenues.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nick.android.rcvenues.Venue;

public class DatabaseHandler extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/com.nick.android.rcvenues/databases/";
	private static String DB_NAME = "venues.db";
	private static String TABLE_NAME = "venues";
	private static String LAST_MOD_TABLE = "mod_date";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private Calendar calendar;
	private InetAddress serverAddress;
	private String serverAdressString = "10.2.105.205";
	private static DatabaseHandler instance;

	public DatabaseHandler(Context context)  {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
		boolean dbexist = checkdatabase();

		if (dbexist) {
			opendatabase();
		} else {
			createdatabase();
		}

	}

	public void createdatabase()  {
		boolean dbexist = checkdatabase();
		if (dbexist) {
			// System.out.println(" Database exists.");
		} else {
			this.getReadableDatabase();
			try {
				copydatabase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkdatabase() {
		// SQLiteDatabase checkdb = null;
		boolean checkdb = false;
		try {
			String myPath = DB_PATH + DB_NAME;
			File dbfile = new File(myPath);
			// checkdb =
			// SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
			checkdb = dbfile.exists();
		} catch (SQLiteException e) {
			System.out.println("Database doesn't exist");
		}

		return checkdb;
	}

	private void copydatabase() throws IOException {

		// Open your local db as the input stream
		InputStream myinput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outfilename = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myoutput = new FileOutputStream(
				"/data/data/com.nick.android.rcvenues/databases/venues.db");

		// transfer byte to inputfile to outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myinput.read(buffer)) > 0) {
			myoutput.write(buffer, 0, length);
		}

		// Close the streams
		myoutput.flush();
		myoutput.close();
		myinput.close();

	}

	public void opendatabase() throws SQLException {
		// Open the database
		String mypath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(mypath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	public synchronized void close() {
		if (myDataBase != null) {
			myDataBase.close();
		}
		super.close();
	}

	public static synchronized DatabaseHandler getHelper(Context context) {
		if (instance == null)
			instance = new DatabaseHandler(context);

		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public ArrayList<Venue> getAllVenues() {

		ArrayList<Venue> venueList = new ArrayList<Venue>();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Venue venue = new Venue();
				venue.setID(cursor.getInt(cursor.getColumnIndex("_id")));
				venue.setName(cursor.getString(cursor.getColumnIndex("name")));
				venue.setDescription(cursor.getString(cursor
						.getColumnIndex("description")));
				venue.setLongitude(cursor.getDouble((cursor
						.getColumnIndex("longitude"))));
				venue.setLatitude(cursor.getDouble((cursor
						.getColumnIndex("latitude"))));
				venue.setType(cursor.getInt((cursor.getColumnIndex("type"))));
				venue.setAddress(cursor.getString((cursor
						.getColumnIndex("address"))));
				int fav = cursor.getInt(cursor.getColumnIndex("favourite"));
				venue.setFavourite(isFavourite(fav));
				venueList.add(venue);
			} while (cursor.moveToNext());
		}

		return venueList;
	}

	private boolean isFavourite(int i) {
		return i == 1;
	}

	public void setFavourite(int id, boolean favourite) {
		SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery;

		if (favourite) {
			updateQuery = "UPDATE " + TABLE_NAME
					+ " set favourite=1 where _id=" + id + ";";
		} else {
			updateQuery = "UPDATE " + TABLE_NAME
					+ " set favourite=0 where _id=" + id + ";";
		}
		db.execSQL(updateQuery);

	}

	public ArrayList<Venue> getFavouriteVenues() {
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME
				+ " WHERE favourite=1;";
		Cursor cursor = db.rawQuery(selectQuery, null);
		ArrayList<Venue> favouriteVenues = new ArrayList<Venue>();
		if (cursor.moveToFirst()) {
			do {
				Venue venue = new Venue();
				venue.setID(cursor.getInt(cursor.getColumnIndex("_id")));
				venue.setName(cursor.getString(cursor.getColumnIndex("name")));
				venue.setDescription(cursor.getString(cursor
						.getColumnIndex("description")));
				venue.setLongitude(cursor.getDouble((cursor
						.getColumnIndex("longitude"))));
				venue.setLatitude(cursor.getDouble((cursor
						.getColumnIndex("latitude"))));
				venue.setType(cursor.getInt((cursor.getColumnIndex("type"))));
				venue.setAddress(cursor.getString((cursor
						.getColumnIndex("address"))));
				int fav = cursor.getInt(cursor.getColumnIndex("favourite"));
				venue.setFavourite(isFavourite(fav));
				favouriteVenues.add(venue);
			} while (cursor.moveToNext());
		}

		return favouriteVenues;
	}

	public Venue getVenueInfo(int id) {
		Venue venue = new Venue();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE _id=\""
				+ id + "\"";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToNext();
		venue.setID(cursor.getInt(cursor.getColumnIndex("_id")));
		venue.setName(cursor.getString(cursor.getColumnIndex("name")));
		venue.setDescription(cursor.getString(cursor
				.getColumnIndex("description")));
		venue.setLongitude(cursor.getDouble((cursor.getColumnIndex("longitude"))));
		venue.setLatitude(cursor.getDouble((cursor.getColumnIndex("latitude"))));
		venue.setType(cursor.getInt((cursor.getColumnIndex("type"))));
		venue.setAddress(cursor.getString((cursor.getColumnIndex("address"))));
		int fav = cursor.getInt(cursor.getColumnIndex("favourite"));
		venue.setFavourite(isFavourite(fav));

		return venue;

	}

	public int getVenueCount() {

		String countQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		return count;
	}

	public String[] getVenueStrings() {
		SQLiteDatabase db = this.getWritableDatabase();
		int count = this.getVenueCount();
		String[] venues = new String[count];
		ArrayList<Venue> venuesAL = this.getAllVenues();

		for (int i = 0; i < count; i++) {
			venues[i] = venuesAL.get(i).toString();
		}
		return venues;
	}

	public ArrayList<Venue> getNearbyVenueNames(int limit, double longitude,
			double latitude) {
		ArrayList<Venue> allVenues = this.getAllVenues();
		ArrayList<Venue> nearbyVenues = new ArrayList<Venue>();
		for (Venue v : allVenues) {
			if (distFrom(latitude, longitude, v.getLatitude(),
					v.getLongitude(), v.getName()) < 50.0) {
				nearbyVenues.add(v);
			}
		}
		return nearbyVenues;

	}

	// Uses Haversine formula
	public static double distFrom(double lat1, double lng1, double lat2,
			double lng2, String name) {
		double earthRadius = 3958.75;

		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lng2 - lng1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = earthRadius * c;

		return d;
	}

	public long getLastMod() {
		SQLiteDatabase db = this.getReadableDatabase();
		String countQuery = "SELECT mod_date FROM " + LAST_MOD_TABLE + ";";
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.moveToNext();

		return cursor.getLong(0);

	}

	public void insertVenues(ArrayList<Venue> venues) {
		SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery;
		for (Venue v : venues) {
			updateQuery = "insert into venues values(null," + "'" + v.getName()
					+ "'," + "'" + v.getDescription() + "'," + "'"
					+ v.getLatitude() + "'," + "'" + v.getLongitude() + "',"
					+ "'" + v.getType() + "', '" + v.getAddress() + "','"
					+ v.isFavourite() + "');";
			db.execSQL(updateQuery);
		}
		String modTimeUpdate = "update mod_date set mod_date="
				+ System.currentTimeMillis() / 1000 + " where id=1;";
		System.out.println(modTimeUpdate);
		db.execSQL(modTimeUpdate);

	}
}