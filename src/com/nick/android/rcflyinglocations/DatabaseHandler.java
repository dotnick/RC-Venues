package com.nick.android.rcflyinglocations;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{
 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.nick.android.rcflyinglocations/databases/";
    private static String DB_NAME = "venuesDB.db";
    private static String TABLE_NAME = "venues";
    private SQLiteDatabase myDataBase; 
    private final Context myContext;
 
   
    public DatabaseHandler(Context context) {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(!dbExist)
 
    			//By calling this method an empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        }
    }
     
 
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READWRITE);
 
    	} catch(SQLiteException e){
 
    		Log.d("DB", "Database doesnt exist.");
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    	
    }
    
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	
    	Log.d("DB", "Copied DB from assets.");
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
    	Log.d("DB", "Opening DB from assets.");
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READWRITE);
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
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
				venue.setName(cursor.getString(0));
				venue.setDescription(cursor.getString(1));
				venue.setlongitude(Integer.parseInt(cursor.getString(2)));
				venue.setLatitude(Integer.parseInt(cursor.getString(3)));
				venue.setType(Integer.parseInt(cursor.getString(4)));
				venue.setCountry(cursor.getString(5));
				venue.setCity(cursor.getString(6));
				venueList.add(venue);
			} while (cursor.moveToNext());
		}
		db.close();
		return venueList;
	}

	public ArrayList<String> getAllVenueNames() {

		ArrayList<String> venueList = new ArrayList<String>();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst()) {
			do {
				String venue = cursor.getString(0);
				venueList.add(venue);
			} while (cursor.moveToNext());
		}
		db.close();
		return venueList;
	}

	public Venue getVenueInfo(String name) {
		Venue venue = new Venue();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE NAME=\"" + name + "\"";
		Log.d("QUERY", selectQuery);
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToNext();
		venue.setName(cursor.getString(0));
		venue.setDescription(cursor.getString(1));
		venue.setlongitude(Integer.parseInt(cursor.getString(2)));
		venue.setLatitude(Integer.parseInt(cursor.getString(3)));
		venue.setType(Integer.parseInt(cursor.getString(4)));
		venue.setCountry(cursor.getString(5));
		venue.setCity(cursor.getString(6));

		db.close();
		return venue;

	}

	public int getVenueCount() {
		String countQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		return cursor.getCount();
	}
 
}