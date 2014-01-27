
package com.jobinbasani.hps.util;

import java.util.List;

import com.jobinbasani.hps.R;
import com.jobinbasani.hps.ReadMoreActivity;
import com.jobinbasani.hps.fragments.WebFragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

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
	
	public static Intent getFeedbackIntent(Context context){
		Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+context.getResources().getString(R.string.feedbackEmail)+"?subject="+Uri.encode(context.getResources().getString(R.string.feedbackSubject))));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.feedbackSubject));
		
		List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(emailIntent, 0);
		//To prevent Receiver leak bug when only application is available for Intent
		if (activities.size() > 1) {
		    // Create and start the chooser
		    return Intent.createChooser(emailIntent, context.getResources().getString(R.string.feedbackIntentTitle));

		  } else {
		    return emailIntent;
		}
	}
	
	public static Intent getPlaystoreListing(String packageName){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id="+packageName));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		return intent;
	}
	
	public static boolean showRateApp(Context context){
		boolean isVisible = true;
		String installedBy = context.getPackageManager().getInstallerPackageName(context.getPackageName());
		if(installedBy == null || !installedBy.equals(context.getResources().getString(R.string.playStore))){
			isVisible = false;
		}
		return isVisible;
	}
	
	public static Intent getBrowserIntent(Context context, String url){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW);
		browserIntent.setData(Uri.parse(url));
		return browserIntent;
	}

}
