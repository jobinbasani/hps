/**
 * 
 */
package com.jobinbasani.hps.database;

import android.provider.BaseColumns;

/**
 * @author jobinbasani
 *
 */
public final class HpsDataContract {

	public HpsDataContract() {
		
	}
	
	public static abstract class HpsDataEntry implements BaseColumns {
        public static final String TABLE_NAME = "hpsentry";
        public static final String COLUMN_NAME_ITEM = "item";
        public static final String COLUMN_NAME_ITEMDATA = "itemtext";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_META = "meta";
        public static final String COLUMN_NAME_MARKER = "marker";
        public static final String COLUMN_NAME_ITEMTYPE = "itemtype";
    }

}
