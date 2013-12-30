/**
 * 
 */
package com.jobinbasani.hps.fragments;

import com.jobinbasani.hps.R;
import com.jobinbasani.hps.adapters.SpellListAdapter;
import com.jobinbasani.hps.database.HpsDbHandler;
import com.jobinbasani.hps.database.HpsDbHelper;
import com.jobinbasani.hps.database.HpsDataContract.HpsDataEntry;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

/**
 * @author jobinbasani
 *
 */
public class SpellsFragment extends ListFragment {
	
	private Cursor cursor;
	private HpsDbHandler dbHandler;
	private SharedPreferences prefs;
	final private static String DB_VERSION_KEY = "dbVersion";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_spells,
				container, false);
		dbHandler = new HpsDbHandler(getActivity());
		prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int dbVersion = prefs.getInt(DB_VERSION_KEY, 0);
		if(dbVersion == HpsDbHelper.DATABASE_VERSION){
			loadSpells();
		}else{
			new DatabaseLoaderTask().execute();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(cursor!=null)
			cursor.close();
		dbHandler.close();
	}
	
	public void loadSpells(){
		dbHandler.open();
		cursor = dbHandler.getAllSpells();
		String[] from = new String[] { HpsDataEntry.COLUMN_NAME_SPELL, HpsDataEntry.COLUMN_NAME_SPELLDATA, HpsDataEntry.COLUMN_NAME_STARTBLOCK, HpsDataEntry.COLUMN_NAME_PHONETICS};
	    int[] to = new int[] { R.id.spellName, R.id.spellData, R.id.spellHeader, R.id.spellPhonetics};
		SpellListAdapter adapter = new SpellListAdapter(getActivity(), R.layout.spell_details, cursor, from, to, SpellListAdapter.NO_SELECTION, cursor.getColumnIndex(HpsDataEntry.COLUMN_NAME_SPELL));
		adapter.setViewBinder(new SpellDetailsViewBinder());
		setListAdapter(adapter);
	}

	private class DatabaseLoaderTask extends AsyncTask<Void, Void, Void>{
		
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
	        pDialog.setMessage("Loading");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			HpsDbHelper hpsDbHelper = new HpsDbHelper(getActivity());
			SQLiteDatabase db = hpsDbHelper.getReadableDatabase(); //Creates or inserts initial data asynchronously
			db.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			pDialog.dismiss();
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt(DB_VERSION_KEY, HpsDbHelper.DATABASE_VERSION);
			editor.commit();
			loadSpells();
		}
		
	}

	private class SpellDetailsViewBinder implements ViewBinder{
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if(view.getId() == R.id.spellHeader){
				String blockLetter = cursor.getString(columnIndex);
				if(blockLetter.equals("0")){
					view.setVisibility(View.GONE);
				}else{
					view.setVisibility(View.VISIBLE);
					TextView headerText = (TextView) view;
					headerText.setText(blockLetter);
				}
				return true;
			}else if(view.getId() == R.id.spellPhonetics){
				String phonetics = cursor.getString(columnIndex);
				if(phonetics.length()>1){
					view.setVisibility(View.VISIBLE);
					TextView spellText = (TextView) view;
					spellText.setText(phonetics);
				}else{
					view.setVisibility(View.GONE);
				}
				return true;
			}
			return false;
		}
		
	}

}
