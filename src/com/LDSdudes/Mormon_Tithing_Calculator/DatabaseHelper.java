/**
 * 
 */
package com.LDSdudes.Mormon_Tithing_Calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Chad
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper{

	static final String dbName="history.db";
	static int dbVersion = 1;
	SQLiteDatabase db;
	
	public static class tithingHistory{
		static final String historyTable = "tithingHistory";
		static final String colID = "_id";
		static final String colnumDate = "HNumDate";
		static final String colDate = "HDate";
		static final String colTithing = "HTithing";
		static final String colFast = "HFast";
		static final String colWMissionary = "HWMissionary";
		static final String colGMissionary = "HGMissionary";
		static final String colBOM = "HBOM";
		static final String colHumanaid = "HHumanaid";
		static final String colTempconst = "HTempconst";
		static final String colEducation = "HEducation";
		static final String colOther = "HOther";
		static final String colTotal = "HTotal";
	}
	
	
	public DatabaseHelper(Context context){
		super(context, dbName, null, dbVersion);
		db = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT," +
				"%s STRING NOT NULL,  %s REAL NOT NULL, %s REAL NOT NULL, %s REAL, %s REAL, %s REAL, %s REAL, %s REAL, %s REAL, %s REAL, %s REAL, %s REAL NOT NULL)",
				tithingHistory.historyTable, tithingHistory.colID, tithingHistory.colDate, tithingHistory.colnumDate, tithingHistory.colTithing,
				tithingHistory.colFast, tithingHistory.colWMissionary, tithingHistory.colGMissionary,
				tithingHistory.colBOM, tithingHistory.colHumanaid, tithingHistory.colTempconst,
				tithingHistory.colEducation, tithingHistory.colOther, tithingHistory.colTotal));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
			
	public void addTithing(String Date,Long numdate, double tithing, double fasting, double wmissionary, double gmissionary, double bom, double humanaid, double tempconst,
			double education, double other, double total){
		ContentValues values = new ContentValues();
		values.put(tithingHistory.colnumDate, numdate);
		values.put(tithingHistory.colDate, Date);
		values.put(tithingHistory.colTithing, tithing);
		values.put(tithingHistory.colFast, fasting);
		values.put(tithingHistory.colWMissionary, wmissionary);
		values.put(tithingHistory.colGMissionary, gmissionary);
		values.put(tithingHistory.colBOM, bom);
		values.put(tithingHistory.colHumanaid, humanaid);
		values.put(tithingHistory.colTempconst, tempconst);
		values.put(tithingHistory.colEducation, education);
		values.put(tithingHistory.colOther, other);
		values.put(tithingHistory.colTotal, total);
		db.insert(tithingHistory.historyTable, null, values);
	}
	
	public Cursor getTithing(Long sDate, Long eDate){
		String query = 
				"Select * " +
				"from tithingHistory " +
				"where " +tithingHistory.colnumDate +" between " + sDate +" and " + eDate +
				" order by " + tithingHistory.colDate + " desc";
		Cursor c = db.rawQuery(query, null);
		return c;
	}
	
	public Cursor getAllTithing(){
		String query = 
				"Select * " +
				"from tithingHistory " +
				"order by " + tithingHistory.colDate + " desc";
		Cursor c = db.rawQuery(query, null);
		return c;	
	}
	
	public void deleteTithing(int historyID){
		String query =
				"Delete " +
				"From " +tithingHistory.historyTable +
				"where " + tithingHistory.colID + " = " + historyID;
				
		db.execSQL(query);
	}
	
	public void modifyTithing(int id, String date, Long numdate, double tithing, double fasting, double wmissionary, double gmissionary, double bom, double humanaid, double tempconst,
			double education, double other){
		
		deleteTithing(id);
		
		double total = tithing + fasting + wmissionary + gmissionary + bom + humanaid + tempconst + education + other;
		addTithing(date, numdate, tithing, fasting, wmissionary, gmissionary, bom, humanaid, tempconst, education, other, total);
				
	}
	
	public Double calcYTD(){
		String query =
			"Select sum(" + tithingHistory.colTotal + ") From " + tithingHistory.historyTable;
		Cursor c = db.rawQuery(query, null);
		if (c.moveToFirst())
		{
			return c.getDouble(0);
		}
		
		return 0.00;
	}
	
	public void close() {
		if (db != null){
			db.close();
		}
	}
}
