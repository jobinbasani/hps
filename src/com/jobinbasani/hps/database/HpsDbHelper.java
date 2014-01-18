/**
 * 
 */
package com.jobinbasani.hps.database;

import com.jobinbasani.hps.R;
import com.jobinbasani.hps.database.HpsDataContract.HpsDataEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author jobinbasani
 *
 */
public class HpsDbHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "HpsData.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private Context context;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + HpsDataEntry.TABLE_NAME + " (" +
            		HpsDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            		HpsDataEntry.COLUMN_NAME_ITEM + TEXT_TYPE + COMMA_SEP +
            		HpsDataEntry.COLUMN_NAME_ITEMDATA + TEXT_TYPE + COMMA_SEP +
            		HpsDataEntry.COLUMN_NAME_LINK + TEXT_TYPE + COMMA_SEP +
            		HpsDataEntry.COLUMN_NAME_PHONETICS + TEXT_TYPE + COMMA_SEP +
            		HpsDataEntry.COLUMN_NAME_STARTBLOCK + TEXT_TYPE + COMMA_SEP +
            		HpsDataEntry.COLUMN_NAME_ITEMTYPE + TEXT_TYPE +
            " )";
        private static final String SQL_DELETE_ENTRIES =
        	    "DROP TABLE IF EXISTS " + HpsDataEntry.TABLE_NAME;

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public HpsDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(SQL_CREATE_ENTRIES);
		
		String[] spellData = context.getResources().getStringArray(R.array.SpellData);
		for(int i=0;i<spellData.length;i++){
			String[] hpsDetails = spellData[i].split("~");
			if(hpsDetails.length == 6){
				ContentValues values = new ContentValues();
				values.put(HpsDataEntry.COLUMN_NAME_ITEM, hpsDetails[0]);
				values.put(HpsDataEntry.COLUMN_NAME_PHONETICS, hpsDetails[1]);
				values.put(HpsDataEntry.COLUMN_NAME_ITEMDATA, hpsDetails[2]);
				values.put(HpsDataEntry.COLUMN_NAME_LINK, hpsDetails[3]);
				values.put(HpsDataEntry.COLUMN_NAME_STARTBLOCK, hpsDetails[4]);
				values.put(HpsDataEntry.COLUMN_NAME_ITEMTYPE, hpsDetails[5]);

				db.insert(
						HpsDataEntry.TABLE_NAME,
				         null,
				         values);
			}
			
		}

	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);

	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
	

}
