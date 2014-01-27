
package com.jobinbasani.hps.fragments;

import java.util.Locale;

import com.jobinbasani.hps.R;
import com.jobinbasani.hps.database.HpsDbHandler;
import com.jobinbasani.hps.database.HpsDataContract.HpsDataEntry;
import com.jobinbasani.hps.util.HpsUtil;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.Toast;

/**
 * @author jobinbasani
 *
 */
public class LinksFragment extends ListFragment {
	
	private Cursor cursor;
	private HpsDbHandler dbHandler;
	private SimpleCursorAdapter listAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_links,
				container, false);
		dbHandler = new HpsDbHandler(getActivity());
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(cursor!=null)
			cursor.close();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadLinks();
		Spinner categorySelector = (Spinner) getActivity().findViewById(R.id.linkTypeChooser);
		categorySelector.setOnItemSelectedListener(new CategorySelector());
	}
	
	private void loadLinks(){
		dbHandler.open();
		Spinner categorySelector = (Spinner) getActivity().findViewById(R.id.linkTypeChooser);
		cursor = dbHandler.getLinks(categorySelector.getSelectedItem().toString());
		String[] from = new String[] { HpsDataEntry.COLUMN_NAME_ITEM, HpsDataEntry.COLUMN_NAME_ITEMDATA, HpsDataEntry.COLUMN_NAME_META, HpsDataEntry.COLUMN_NAME_MARKER, HpsDataEntry._ID};
	    int[] to = new int[] { R.id.linkName, R.id.linkData, R.id.linkCategory, R.id.linkOptions, R.id.favLink};
	    listAdapter = new SimpleCursorAdapter(getActivity(), R.layout.links_details, cursor, from, to, SimpleCursorAdapter.NO_SELECTION);
	    listAdapter.setViewBinder(new LinkDetailsViewBinder());
		setListAdapter(listAdapter);
	}
	
	private void updateLinksList(String category){
		cursor = dbHandler.getLinks(category);
		listAdapter.swapCursor(cursor);
		listAdapter.notifyDataSetChanged();
	}
	
	private class LinkDetailsViewBinder implements ViewBinder{

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if(view.getId() == R.id.linkCategory){
				TextView linkCategory = (TextView) view;
				Locale l = Locale.getDefault();
				linkCategory.setText(cursor.getString(columnIndex).toUpperCase(l));
				linkCategory.setBackgroundColor(getCategoryColor(cursor.getString(columnIndex)));
				return true;
			}else if(view.getId() == R.id.linkOptions){
				final PopupMenu popupMenu = new PopupMenu(getActivity(), view);
				popupMenu.getMenuInflater().inflate(R.menu.linkdetailsmenu, popupMenu.getMenu());
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						popupMenu.show();
					}
				});
				popupMenu.setOnMenuItemClickListener(new LinkDetailsMenuClick(view.getParent()));
				return true;
			}else if(view.getId() == R.id.linkData){
				TextView linkData = (TextView) view;
				final String url = cursor.getString(cursor.getColumnIndex(HpsDataEntry.COLUMN_NAME_LINK));
				view.setTag(url);
				linkData.setText(cursor.getString(columnIndex));
				view.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startActivity(HpsUtil.getReadMoreIntent(getActivity(), url));
					}
				});
				return true;
			}else if(view.getId() == R.id.favLink){
				CheckBox favoriteSelector = (CheckBox) view;
				view.setTag(cursor.getString(columnIndex));
				int favFlag = cursor.getInt(cursor.getColumnIndex(HpsDataEntry.COLUMN_NAME_MARKER));
				favoriteSelector.setChecked(favFlag==1?true:false);
				favoriteSelector.setOnClickListener(new FavoriteHandler());
				return true;
			}
			return false;
		}
		
		private int getCategoryColor(String category){
			if(category.equals("Official")){
				return getResources().getColor(R.color.violetColor);
			}else if(category.equals("Hogwarts")){
				return getResources().getColor(R.color.greenColor);
			}else if(category.equals("General")){
				return getResources().getColor(R.color.orangeColor);
			}else if(category.equals("Wiki")){
				return getResources().getColor(R.color.redColor);
			}else if(category.equals("Podcast")){
				return getResources().getColor(R.color.blueColor);
			}else if(category.equals("Encyclopedia")){
				return getResources().getColor(R.color.magentaColor);
			}else if(category.equals("RPG")){
				return getResources().getColor(R.color.yellowOrangeColor);
			}else if(category.equals("Fanfiction")){
				return getResources().getColor(R.color.yellowColor);
			}
			else
				return getResources().getColor(R.color.violetColor);
		}
		
	}
	
	private class LinkDetailsMenuClick implements OnMenuItemClickListener{
		
		private RelativeLayout rl;
		
		public LinkDetailsMenuClick(ViewParent view){
			rl = (RelativeLayout) view;
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			TextView linkData = (TextView) rl.findViewById(R.id.linkData);
			TextView linkName = (TextView) rl.findViewById(R.id.linkName);
			switch(item.getItemId()){
			case R.id.linkDetailsOpenLink:
				startActivity(HpsUtil.getReadMoreIntent(getActivity(), linkData.getTag().toString()));
				return true;
			case R.id.linkDetailsOpenBrowser:
				startActivity(HpsUtil.getBrowserIntent(linkData.getTag().toString()));
				return true;
			case R.id.linkDetailsShare:
				startActivity(Intent.createChooser(HpsUtil.getShareDataIntent(linkName.getText()+" - "+linkData.getTag()), getResources().getString(R.string.shareLinkDetails)));
				return true;
			}
			return false;
		}
		
	}
	
	private class FavoriteHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			Spinner categorySelector = (Spinner) getActivity().findViewById(R.id.linkTypeChooser);
			boolean isChecked = ((CheckBox) v).isChecked();
			Toast.makeText(getActivity(), "Favorite "+(isChecked==true?"added...":"removed..."), Toast.LENGTH_SHORT).show();
			dbHandler.updateFavorite(v.getTag().toString(), isChecked==true?"1":"0");
			if(categorySelector.getSelectedItem().toString().equals("Favorites")){
				updateLinksList(categorySelector.getSelectedItem().toString());
			}
		}
		
	}
	
	private class CategorySelector implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> adapter, View v, int pos,
				long id) {
			updateLinksList(adapter.getSelectedItem().toString());
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapter) {
			
		}
		
	}

}
