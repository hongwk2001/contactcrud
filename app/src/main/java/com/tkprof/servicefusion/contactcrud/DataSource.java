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

  public void  saveLine(int cur_line_no, String p_content, String l_insOrupd){

      if (l_insOrupd.equals("Ins")){
    	  cur_line_no = LAST_ROW_NO + 1;
      }
	  
	  Log.i("datasource.saveLIne", cur_line_no + " " + p_content) ;
	  
	    String[] ids = {  p_content, ""+cur_line_no };


	    String sql;
        if (l_insOrupd.equals("Upd")){
          sql = "update contacts set text1 = ?" +
        		"where  _no = ?";
        }else{
        	sql = "insert into contacts " +
        			" ( text1, _no)  " +
        			"values ( ? , ? ) ";
        }
        		
		database.execSQL(sql, ids);
  }
  
  // id as search condition.
  public ContactRecord  getContact(int id ) {
	ContactRecord row = new ContactRecord();

	String[] ids = {""+id};

	Cursor cursor = database.rawQuery (
	"select _no, Pum_no, Phase_no, Text1 " +
	   " from content c " +
	   "where c._no = ? " +
	   "order by _no"
		, ids );

	cursor.moveToFirst();
	ContactRecord eachRow = cursorToRow(cursor);

	// make sure to close the cursor
	cursor.close();
	return eachRow;
  }
  

  public int getNewlineno(){ 
	  String[] arg = {};  // no need
	  
	  Cursor cursor = database.rawQuery (
	           "select max(_no) +1 from content c ", arg );
	  
	    int line_no = 0;
	    boolean datafound = cursor.moveToFirst();
		if (datafound) {
		    	line_no = cursor.getInt(0); 
		 }
		 // make sure to close the cursor
		 cursor.close();
		 return line_no;
  }
  
  public int getLastLineNo(){  
		 return getNewlineno() - 1;
  }
		  
  public int getLineNoByPumNo(int pum_no){
	  String[] pum_nos = {""+pum_no};
	    
	    Cursor cursor = database.rawQuery (
          "select min(_no) " + 
	           " from content c " +
	           "where c.pum_no = ? "   
	    		, pum_nos );
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
}