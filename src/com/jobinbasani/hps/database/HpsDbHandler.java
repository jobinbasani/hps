/**
 * 
 */
package com.jobinbasani.hps.database;

import com.jobinbasani.hps.database.HpsDataContract.HpsDataEntry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author jobinbasani
 *
 */
public class HpsDbHandler {
	
	private Context mContext;
	private HpsDbHelper mDbHelper;
	private SQLiteDatabase mSQLiteDatabase;

	public HpsDbHandler(Context context) {
		this.mContext = context;
		mDbHelper = new HpsDbHelper(this.mContext);
	}
	
	public void open(){
		if(mSQLiteDatabase==null)
			mSQLiteDatabase = mDbHelper.getReadableDatabase(); 
	}
	
	public void close(){
		if(mSQLiteDatabase!=null)
			mSQLiteDatabase.close();
	}
	
	public Cursor getAllSpells(){
		return mSQLiteDatabase.rawQuery("SELECT * FROM "+HpsDataEntry.TABLE_NAME+" WHERE "+HpsDataEntry.COLUMN_NAME_ITEMTYPE+"=? ORDER BY "+HpsDataEntry.COLUMN_NAME_ITEM, new String[]{"S"});
	}
	
	public Cursor getAllPotions(){
		return mSQLiteDatabase.rawQuery("SELECT * FROM "+HpsDataEntry.TABLE_NAME+" WHERE "+HpsDataEntry.COLUMN_NAME_ITEMTYPE+"=? ORDER BY "+HpsDataEntry.COLUMN_NAME_ITEM, new String[]{"P"});
	}

}
