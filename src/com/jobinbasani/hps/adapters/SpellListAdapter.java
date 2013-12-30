/**
 * 
 */
package com.jobinbasani.hps.adapters;

import android.content.Context;
import android.database.Cursor;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;

/**
 * @author jobinbasani
 *
 */
public class SpellListAdapter extends SimpleCursorAdapter implements
		SectionIndexer {
	AlphabetIndexer alphaIndexer;

	/**
	 * @param context
	 * @param layout
	 * @param c
	 * @param from
	 * @param to
	 * @param flags
	 */
	public SpellListAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, int sortedColumnIndex) {
		super(context, layout, c, from, to, flags);
		alphaIndexer = new AlphabetIndexer(c, sortedColumnIndex,
                " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	/* (non-Javadoc)
	 * @see android.widget.SectionIndexer#getPositionForSection(int)
	 */
	@Override
	public int getPositionForSection(int section) {
		return alphaIndexer.getPositionForSection(section);
	}

	/* (non-Javadoc)
	 * @see android.widget.SectionIndexer#getSectionForPosition(int)
	 */
	@Override
	public int getSectionForPosition(int position) {
		return alphaIndexer.getSectionForPosition(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.SectionIndexer#getSections()
	 */
	@Override
	public Object[] getSections() {
		return alphaIndexer.getSections();
	}

}
