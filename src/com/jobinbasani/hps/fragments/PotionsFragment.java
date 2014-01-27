
package com.jobinbasani.hps.fragments;

import java.util.HashMap;

import com.jobinbasani.hps.R;
import com.jobinbasani.hps.adapters.HpsListAdapter;
import com.jobinbasani.hps.constants.HpsConstants;
import com.jobinbasani.hps.database.HpsDbHandler;
import com.jobinbasani.hps.database.HpsDataContract.HpsDataEntry;
import com.jobinbasani.hps.util.HpsUtil;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.SimpleCursorAdapter.ViewBinder;

/**
 * @author jobinbasani
 *
 */
public class PotionsFragment extends ListFragment {
	
	private Cursor cursor;
	private HpsDbHandler dbHandler;
	private ActionMode mActionMode;
	private Integer selectedId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_potions,
				container, false);
		dbHandler = new HpsDbHandler(getActivity());
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setSelector(R.color.listSelectedBg);
		loadPotions();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selectedId = -1;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		TextView potionNameText = (TextView) v.findViewById(R.id.potionsName);
		Integer currentSelectedId = (Integer) potionNameText.getTag();
		
		if(mActionMode == null){
			mActionMode = getActivity().startActionMode(mActionModeCallback);
		}
		if(selectedId == currentSelectedId){
			mActionMode.finish();
			selectedId = -1;
		}else{
			selectedId = currentSelectedId;
			TextView potionDataText = (TextView) v.findViewById(R.id.potionsData);
			HashMap<String, String> shareData = new HashMap<String, String>();
			shareData.put(HpsConstants.SHARE_TEXT_KEY, potionNameText.getText()+" - "+potionDataText.getText()+" Read more at "+potionDataText.getTag());
			shareData.put(HpsConstants.POTION_LINK_KEY, potionDataText.getTag()+"");
			mActionMode.setTag(shareData);
		}
		
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(!isVisibleToUser && mActionMode!=null){
				mActionMode.finish();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(cursor!=null)
			cursor.close();
	}
	
	private void loadPotions(){
		dbHandler.open();
		cursor = dbHandler.getAllPotions();
		String[] from = new String[] { HpsDataEntry.COLUMN_NAME_ITEM, HpsDataEntry.COLUMN_NAME_ITEMDATA, HpsDataEntry.COLUMN_NAME_MARKER};
	    int[] to = new int[] { R.id.potionsName, R.id.potionsData, R.id.potionsHeader};
		HpsListAdapter adapter = new HpsListAdapter(getActivity(), R.layout.potions_details, cursor, from, to, HpsListAdapter.NO_SELECTION, cursor.getColumnIndex(HpsDataEntry.COLUMN_NAME_ITEM));
		adapter.setViewBinder(new PotionsDetailsViewBinder());
		setListAdapter(adapter);
	}
	
	private class PotionsDetailsViewBinder implements ViewBinder{
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if(view.getId() == R.id.potionsHeader){
				String blockLetter = cursor.getString(columnIndex);
				if(blockLetter.equals("0")){
					view.setVisibility(View.GONE);
				}else{
					view.setVisibility(View.VISIBLE);
					TextView headerText = (TextView) view;
					headerText.setText(blockLetter);
				}
				return true;
			}else if(view.getId() == R.id.potionsName){
				TextView potionNameText = (TextView) view;
				potionNameText.setText(cursor.getString(columnIndex));
				potionNameText.setTag(cursor.getInt(cursor.getColumnIndex(HpsDataEntry._ID)));
				return true;
			}else if(view.getId() == R.id.potionsData){
				TextView potionDataText = (TextView) view;
				potionDataText.setText(cursor.getString(columnIndex));
				potionDataText.setTag(cursor.getString(cursor.getColumnIndex(HpsDataEntry.COLUMN_NAME_LINK)));
				return true;
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
			inflater.inflate(R.menu.potionsmenu, menu);
			return true;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			HashMap<String, String> shareData = (HashMap<String, String>) mActionMode.getTag();
			switch(item.getItemId()){
			case R.id.potionsMenuSharePotion:
				startActivity(Intent.createChooser(HpsUtil.getShareDataIntent(shareData.get(HpsConstants.SHARE_TEXT_KEY)), getResources().getString(R.string.sharePotion)));
				mode.finish();
				return true;
			case R.id.potionsMenuReadMore:
				startActivity(HpsUtil.getReadMoreIntent(getActivity(), shareData.get(HpsConstants.POTION_LINK_KEY)));
				mode.finish();
				return true;
			}
			return false;
		}
	};

}
