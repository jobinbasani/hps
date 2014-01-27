package com.jobinbasani.hps;

import com.google.analytics.tracking.android.EasyTracker;
import com.jobinbasani.hps.util.HpsUtil;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class ReadMoreActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_more);
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_more, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.readMenuFeedback:
			startActivity(HpsUtil.getFeedbackIntent(this));
			return true;
		case R.id.readMenuRateApp:
			startActivity(HpsUtil.getPlaystoreListing(getPackageName()));
			return true;
		case R.id.readMenuOpenBrowser:
			WebView webView = (WebView) findViewById(R.id.webView);
			startActivity(HpsUtil.getBrowserIntent(webView.getUrl()));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.readMenuRateApp).setVisible(HpsUtil.showRateApp(this));
		return super.onPrepareOptionsMenu(menu);
	}

}
