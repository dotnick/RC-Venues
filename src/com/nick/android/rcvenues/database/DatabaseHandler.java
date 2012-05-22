package com.nick.android.rcvenues.database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHandler extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/com.nick.android.rcvenues/databases/";
	private static String DB_NAME = "venues.db";
	private static String TABLE_NAME = "venues";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private Calendar calendar;
	private InetAddress serverAddress;
	private String serverAdressString = "10.2.105.205";

	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
		calendar = new GregorianCalendar();
		try {
			serverAddress = InetAddress.getByName(serverAdressString);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();
		if (!dbExist)
			// By calling this method an empty database will be created into the
			// default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();
		try {
			copyDataBase(false);
		} catch (IOException e) {
			throw new Error("Error copying database");
		}
	}

	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);

		} catch (SQLiteException e) {
			Log.d("DB", "Database doesnt exist.");
		}

		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase(boolean fromSync) throws IOException {
		String TEMP_FILENAME = "db.temp";
		if(fromSync) {
			FileInputStream fis = myContext.openFileInput(TEMP_FILENAME);
			FileOutputStream fos = myContext.openFileOutput(DB_NAME, Context.MODE_PRIVATE);
			fos.write(fis.read());
		} else {
			InputStream is = myContext.getAssets().open(DB_NAME);
			
			// Path to the just created empty db
			String outFileName = DB_PATH + DB_NAME;
	
			// Open the empty db as the output stream
			OutputStream os = new FileOutputStream(outFileName);
	
			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			
			os.flush();
			os.close();
			is.close();
		}
		
	

	}

	public void openDataBase() throws SQLException {
	
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

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

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				Venue venue = new Venue();
				venue.setID(cursor.getInt(cursor.getColumnIndex("_id")));
				venue.setName(cursor.getString(cursor.getColumnIndex("name")));
				venue.setDescription(cursor.getString(cursor.getColumnIndex("description")));
				venue.setLongitude(cursor.getDouble((cursor.getColumnIndex("longitude"))));
				venue.setLatitude(cursor.getDouble((cursor.getColumnIndex("latitude"))));
				venue.setType(cursor.getInt((cursor.getColumnIndex("type"))));
				venue.setAddress(cursor.getString((cursor.getColumnIndex("address"))));
				int fav = cursor.getInt(cursor.getColumnIndex("favourite"));
				venue.setFavourite(isFavourite(fav));
				venueList.add(venue);
			} while (cursor.moveToNext());
		}
		db.close();
		return venueList;
	}
	private boolean isFavourite(int i) {
		return i==1;
	}
	
	public void setFavourite(int id, boolean favourite) {
		SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery;
		
		if(favourite) {
			updateQuery="UPDATE " + TABLE_NAME + " set favourite=1 where _id=" + id + ";";
		} else {
			updateQuery="UPDATE " + TABLE_NAME + " set favourite=0 where _id=" + id + ";";
		}
		db.execSQL(updateQuery);
		db.close();
	}
	
	public ArrayList<Venue> getFavouriteVenues() {
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE favourite=1;";
		Cursor cursor = db.rawQuery(selectQuery, null);
		ArrayList<Venue> favouriteVenues = new ArrayList<Venue>();
		
		if (cursor.moveToFirst()) {
			do {
				Venue venue = new Venue();
				venue.setID(cursor.getInt(cursor.getColumnIndex("_id")));
				venue.setName(cursor.getString(cursor.getColumnIndex("name")));
				venue.setDescription(cursor.getString(cursor.getColumnIndex("description")));
				venue.setLongitude(cursor.getDouble((cursor.getColumnIndex("longitude"))));
				venue.setLatitude(cursor.getDouble((cursor.getColumnIndex("latitude"))));
				venue.setType(cursor.getInt((cursor.getColumnIndex("type"))));
				venue.setAddress(cursor.getString((cursor.getColumnIndex("address"))));
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
		Log.d("QUERY", selectQuery);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToNext();
		venue.setID(cursor.getInt(cursor.getColumnIndex("_id")));
		venue.setName(cursor.getString(cursor.getColumnIndex("name")));
		venue.setDescription(cursor.getString(cursor.getColumnIndex("description")));
		venue.setLongitude(cursor.getDouble((cursor.getColumnIndex("longitude"))));
		venue.setLatitude(cursor.getDouble((cursor.getColumnIndex("latitude"))));
		venue.setType(cursor.getInt((cursor.getColumnIndex("type"))));
		venue.setAddress(cursor.getString((cursor.getColumnIndex("address"))));
		int fav = cursor.getInt(cursor.getColumnIndex("favourite"));
		venue.setFavourite(isFavourite(fav));
		db.close();
		return venue;

	}

	public int getVenueCount() {

		String countQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}

	public String[] getVenueStrings() {
		SQLiteDatabase db = this.getWritableDatabase();
		int count = this.getVenueCount();
		String[] venues = new String[count];
		ArrayList<Venue> venuesAL = this.getAllVenues();
		db.close();

		for (int i = 0; i < count; i++) {
			venues[i] = venuesAL.get(i).toString();
		}
		return venues;
	}

	public ArrayList<Venue> getNearbyVenueNames(int limit, double longitude, double latitude) {
		ArrayList<Venue> allVenues = this.getAllVenues();
		ArrayList<Venue> nearbyVenues = new ArrayList<Venue>();
		for(Venue v : allVenues) {
			if(distFrom(latitude , longitude , v.getLatitude() , v.getLongitude() , v.getName()) < 50.0) {
				nearbyVenues.add(v);
			}
		}
		return nearbyVenues;

	}

	// Uses Haversine formula
	public static double distFrom(double lat1, double lng1, double lat2, double lng2, String name) {
	    double earthRadius = 3958.75;
	    
	    double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lng2-lng1);
        
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
                Math.sin(dLon/2) * Math.sin(dLon/2); 
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        double d = earthRadius * c;             
       
	 	return d;
	    }
	
	public byte[] checkForUpdate() {
		Socket toServer = null;
		try {
			 toServer = new Socket(serverAddress, 8989);
			 Log.d("Sync", "Set up socket");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(toServer.getOutputStream());
			output.flush();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream(toServer.getInputStream());
		} catch (StreamCorruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Long lastModifiedTest = (long) 12345;
		
		String mod_request = "mod_date";
		try {
			
			Log.d("Sync", "Set up outputstream");
			output.writeObject(mod_request);
			output.flush();
			Log.d("Sync", "wrote request object");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Long reply = new Long(999999999);
		try {
			
			Log.d("Sync", "got inputstream");
			reply = (Long) input.readObject();
			Log.d("Sync", "read reply");
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if(reply > lastModifiedTest) {
			Log.d("Server Sync", "Syncing db with server.");
			String request = "sync";
			try {
				output.writeObject(request);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] syncReply = null;
			try {
				syncReply = (byte[]) input.readObject();
			} catch (OptionalDataException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return syncReply;			
		}
		
		return null;
	}

}