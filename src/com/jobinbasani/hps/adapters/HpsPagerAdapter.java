/**
 * 
 */
package com.jobinbasani.hps.adapters;

import java.util.Locale;

import com.jobinbasani.hps.R;
import com.jobinbasani.hps.fragments.PotionsFragment;
import com.jobinbasani.hps.fragments.SpellsFragment;
import com.jobinbasani.hps.fragments.WebFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author jobinbasani
 * 
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 *
 */
public class HpsPagerAdapter extends FragmentPagerAdapter {
	
	private Context context;

	public HpsPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		
		Fragment fragment = null;
		
		switch(position){
		case 0:
			fragment = new SpellsFragment();
			break;
		case 1:
			fragment = new PotionsFragment();
			break;
		case 2:
			fragment = new WebFragment();
			Bundle args = new Bundle();
			args.putString(WebFragment.URL, context.getResources().getString(R.string.newsPage));
			fragment.setArguments(args);
			break;
		case 3:
			fragment = new WebFragment();
			Bundle argsTweet = new Bundle();
			argsTweet.putString(WebFragment.URL, context.getResources().getString(R.string.twitterPage));
			fragment.setArguments(argsTweet);
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return context.getResources().getStringArray(R.array.FragmentTitles).length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		return context.getResources().getStringArray(R.array.FragmentTitles)[position].toUpperCase(l);
	}
}

