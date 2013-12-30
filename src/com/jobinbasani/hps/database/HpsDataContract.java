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
        public static final String COLUMN_NAME_SPELL = "spell";
        public static final String COLUMN_NAME_SPELLDATA = "spelltext";
        public static final String COLUMN_NAME_LINK = "link";
    }

}
