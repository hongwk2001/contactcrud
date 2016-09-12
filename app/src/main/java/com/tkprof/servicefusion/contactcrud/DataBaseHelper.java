package com.tkprof.servicefusion.contactcrud;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper{

	//The Android's default system path of your application database.
	private static String DB_PATH ;
	private static String DB_NAME = "contacts.db";

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	public static final String TABLE_CONTACTS = "contacts";

	public static final String COLUMN_NO = "_no";
	public static final String COLUMN_FIRST_NAME = "first_name";
	public static final String COLUMN_LAST_NAME = "last_name";
	public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
	public static final String COLUMN_ZIP_CODE = "zip_code";

	private static final int DATABASE_VERSION = 1;

	/**
	 * Constructor
	 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	 * @param context
	 */
	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.myContext = context;
		DB_PATH = myContext.getDatabasePath(DB_NAME).toString() ;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 * */
	public void createDataBase() throws IOException{

		boolean dbExist = checkDataBase();

		if(dbExist){
			//do nothing - database already exist
		}else{
			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	private boolean checkDataBase(){
		boolean checkdb = false;
		try{
			String myPath = DB_PATH ; /* myContext.getFilesDir().getAbsolutePath().replace("files", "databases")+File.separator + DB_NAME;*/
			File dbfile = new File(myPath);
			checkdb = dbfile.exists();
		}
		catch(SQLiteException e){
			System.out.println("Database doesn't exist");
		}
		return checkdb;
	}

	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException{

		Log.i("copydb DB_NAME", DB_NAME);
		//Open your local db as the input stream
		//InputStream myInput = myContext.getAssets().open(DB_NAME);
		AssetManager am = myContext.getAssets();
		String fn = String.format(DB_NAME);
		InputStream myInput = am.open(fn);

		Log.i("copydb myInput", myInput.toString());

		// Path to the just created empty db
		String outFileName = DB_PATH /*+ DB_NAME*/;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}


	public void openDataBase() throws SQLException{

		//Open the database
		String myPath = DB_PATH ;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

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

	// Add your public helper methods to access and get content from the database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
	// to you to create adapters for your views.

}