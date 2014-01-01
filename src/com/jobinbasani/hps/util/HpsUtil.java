
package com.jobinbasani.hps.util;

import com.jobinbasani.hps.ReadMoreActivity;
import com.jobinbasani.hps.fragments.WebFragment;

import android.content.Context;
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
	
	public static Intent getReadMoreIntent(Context context, String url){
		Intent readMoreIntent = new Intent(context,ReadMoreActivity.class);
		readMoreIntent.putExtra(WebFragment.URL, url);
		return readMoreIntent;
	}

}
