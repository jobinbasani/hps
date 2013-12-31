/**
 * 
 */
package com.jobinbasani.hps.fragments;

import com.jobinbasani.hps.R;
import com.jobinbasani.hps.adapters.SpellListAdapter;
import com.jobinbasani.hps.database.HpsDbHandler;
import com.jobinbasani.hps.database.HpsDataContract.HpsDataEntry;
import com.jobinbasani.hps.database.HpsDbHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

/**
 * @author jobinbasani
 *
 */
public class SpellsFragment extends ListFragment{
	
	private Cursor cursor;
	private HpsDbHandler dbHandler;
	private ActionMode mActionMode;
	private Integer selectedId;
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		TextView spellNameText = (TextView) v.findViewById(R.id.spellName);
		Integer currentSelectedId = (Integer) spellNameText.getTag();
		
		if(mActionMode == null){
			mActionMode = getActivity().startActionMode(mActionModeCallback);
		}
		if(selectedId == currentSelectedId){
			mActionMode.finish();
			selectedId = -1;
		}else{
			selectedId = currentSelectedId;
		}
		
	}

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selectedId = -1;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setSelector(android.R.color.holo_blue_light);
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
			HpsDbHandler dbHandler = new HpsDbHandler(getActivity());
			dbHandler.open();
			dbHandler.close();
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
			}else if(view.getId() == R.id.spellName){
				TextView spellNameText = (TextView) view;
				spellNameText.setText(cursor.getString(columnIndex));
				spellNameText.setTag(cursor.getInt(cursor.getColumnIndex(HpsDataEntry._ID)));
			}
			return false;
		}
		
	}
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}
		
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			getListView().clearFocus();
			mActionMode = null;
		}
		
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.spellmenu, menu);
			return true;
		}
		
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			return false;
		}
	};

}
