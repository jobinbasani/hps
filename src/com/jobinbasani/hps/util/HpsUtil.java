
package com.jobinbasani.hps.util;

import android.content.Intent;

/**
 * @author jobinbasani
 *
 */
public class HpsUtil {

	public static Intent getShareDataIntent(String data){
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, data);
		shareIntent.setType("text/plain");
		return shareIntent;
	}

}
