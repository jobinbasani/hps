/**
 * 
 */
package com.jobinbasani.hps.database;

import com.jobinbasani.hps.database.HpsDataContract.HpsDataEntry;

import android.content.ContentValues;
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
		mDbHelper = HpsDbHelper.getInstance(mContext);
	}
	
	public void open(){
		if(mSQLiteDatabase == null){
			mSQLiteDatabase = mDbHelper.getWritableDatabase();
		}
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
	
	public Cursor getLinks(String category){
		String filter = "";
		String[] args = new String[]{"L"};
		if(category.equals("Favorites")){
			filter = " AND "+HpsDataEntry.COLUMN_NAME_MARKER+"=? ";
			args = new String[]{"L","1"};
		}else if(!category.equals("All Links")){
			filter = " AND "+HpsDataEntry.COLUMN_NAME_META+"=? ";
			args = new String[]{"L",category};
		}
		return mSQLiteDatabase.rawQuery("SELECT * FROM "+HpsDataEntry.TABLE_NAME+" WHERE "+HpsDataEntry.COLUMN_NAME_ITEMTYPE+"=? "+filter+" ORDER BY "+HpsDataEntry.COLUMN_NAME_META+", "+HpsDataEntry.COLUMN_NAME_ITEM, args);
	}
	
	public void updateFavorite(String rowId, String flag){
		ContentValues values = new ContentValues();
		values.put(HpsDataEntry.COLUMN_NAME_MARKER, flag);
		mSQLiteDatabase.update(HpsDataEntry.TABLE_NAME, values, HpsDataEntry._ID+"="+rowId, null);
	}

}
