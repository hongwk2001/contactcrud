package com.tkprof.servicefusion.contactcrud;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSource {

  // Database fields
  private SQLiteDatabase database;
  private DataBaseHelper dbHelper;
  private String[] allColumns = {
		  DataBaseHelper.COLUMN_NO      ,
		  DataBaseHelper.COLUMN_FIRST_NAME  ,
		  DataBaseHelper.COLUMN_LAST_NAME,
		  DataBaseHelper.COLUMN_DATE_OF_BIRTH,
          DataBaseHelper.COLUMN_ZIP_CODE };
  
  public int LAST_ROW_NO = 0;

  public DataSource(Context context) {
    dbHelper = new DataBaseHelper(context); 
 		
	try { 
		dbHelper.createDataBase(); 
	} catch (IOException ioe) {
		throw new Error("Unable to create database");
	}
	
	try { 
		dbHelper.openDataBase(); 
	}catch(SQLException sqle){
		throw sqle; 
	}
	//-------------------
  }
 
  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public String getLocale(){
	  String[] arg = { };  // no need
	  
	  Cursor cursor = database.rawQuery (
	           "select lan1 From locales ", arg );
	  
	    String l_locale ="";
	    boolean datafound = cursor.moveToFirst();
		if (datafound) {
			l_locale = cursor.getString(0); 
		 }
		Log.d("DAatasource.getLocale"," " + l_locale);
		 // make sure to close the cursor
		 cursor.close();
		 return l_locale; 
  }
  
  public void deleteRow(ContactRecord row) {
    int _no = row.getKey() ;
    Log.i("delete", "Comment deleted with _no: " + _no);
    database.delete(DataBaseHelper.TABLE_CONTACTS, DataBaseHelper.COLUMN_NO
         + " = '" + _no +"'", null);
  }
  
  public void deleteLine(int l_line) { 
	  String[] ids = { ""+l_line };
	    Log.i("delete", "Comment deleted with _no: " + l_line);
	    database.delete(DataBaseHelper.TABLE_CONTACTS, DataBaseHelper.COLUMN_NO
	         + " = ? ", ids);
	  }

  // id as search condition.
  public ContactRecord  getContact(int id ) {
	ContactRecord row = new ContactRecord();

	String[] ids = {""+id};

	Cursor cursor = database.rawQuery (
			"select _id," +
					" first_name, last_name,  " +
					" date_of_birth, zip_code  " +
					" from contacts  c " +
					"where c._id = ? " +
					"order by _id"
		, ids );

	cursor.moveToFirst();
	ContactRecord eachRow = cursorToRow(cursor);

	// make sure to close the cursor
	cursor.close();
	return eachRow;
  }


	public int getLastLineNo(){
		return getNewlineno() - 1;
	}

	public int getNewlineno(){
		String [] arg = {};  // no need

		Cursor cursor = database.rawQuery (
				"select max(_id) +1 from contacts c ", arg );

		int line_no = 0;
		boolean datafound = cursor.moveToFirst();
		if (datafound) {
			line_no = cursor.getInt(0);
		}
		// make sure to close the cursor
		cursor.close();
		return line_no;
	}


	private ContactRecord cursorToRow(Cursor cursor) {
	  ContactRecord newrow = new ContactRecord();

	  newrow.setKey(cursor.getInt(0));
	  newrow.setFirstName (cursor.getString(1));
	  newrow.setLastName(cursor.getString(2));
	  newrow.setDateOfBirth(cursor.getString(3));
	  newrow.setZipCode(cursor.getString(4));
    return newrow;
  }

	public List<ContactRecord> getContacts() {
		List<ContactRecord> rows = new ArrayList<ContactRecord>();

		Cursor cursor = database.rawQuery (
				"select _id," +
						" first_name, last_name,  " +
						" date_of_birth, zip_code  " +
						" from contacts  " +
						"order by _id"
				, null);

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			ContactRecord eachrow = cursorToRow(cursor);
			rows.add(eachrow);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return rows;
	}

	public void updateContact(int id, String first_name, String last_name,
							  String date_of_birth, String zip_code) {

		Log.i("datasource.saveLIne", id + " " + first_name) ;

		String[] ids = { first_name,last_name,
		                  date_of_birth, zip_code,  ""+id };

		String sql;
			sql = "update contacts " +
					"set first_name = ? ," +
					"   last_name = ?  ," +
					"   date_of_birth = ? ," +
					"   zip_code = ? " +
					"where _id = ?";

		database.execSQL(sql, ids);
	}
	public void insertContact(String first_name, String last_name,
							  String date_of_birth, String zip_code) {

		Log.i("datasource.insContact",
			"firstN:" + first_name + " Lname" + last_name
		        + " dob:" + date_of_birth + " " + zip_code)
		;

		String[] ids = { first_name,last_name,
				date_of_birth, zip_code  };

		String sql;

			sql = "insert into contacts " +
					" (first_name, last_name, " +
					" date_of_birth, zip_code)  " +
					"values " +
					"( ?, ?, ?, ? ) ";

		database.execSQL(sql, ids);
	}
}