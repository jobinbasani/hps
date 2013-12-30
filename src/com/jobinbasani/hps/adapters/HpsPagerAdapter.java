/**
 * 
 */
package com.jobinbasani.hps.adapters;

import java.util.Locale;

import com.jobinbasani.hps.R;
import com.jobinbasani.hps.fragments.SpellsFragment;
import com.jobinbasani.hps.fragments.WebFragment;

import android.content.Context;
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
		
		Fragment fragment;
		
		switch(position){
		case 0:
			fragment = new SpellsFragment();
			break;
		default:
			fragment = new WebFragment();
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

