/**
 * 
 */
package com.jobinbasani.hps.adapters;

import java.util.Locale;

import com.jobinbasani.hps.R;
import com.jobinbasani.hps.fragments.SpellsFragment;

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
		
		Fragment fragment = new SpellsFragment();
		Bundle args = new Bundle();
		args.putInt(SpellsFragment.ARG_SECTION_NUMBER, position + 1);
		fragment.setArguments(args);
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

