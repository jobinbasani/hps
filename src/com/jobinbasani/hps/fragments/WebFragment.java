/**
 * 
 */
package com.jobinbasani.hps.fragments;

import com.jobinbasani.hps.R;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @author jobinbasani
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebFragment extends Fragment {
	
	final public static String URL = "url";
	private WebView webView;
	private ProgressBar progressBar;
	private String url;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_web,
				container, false);
		try{
			this.url = getArguments().getString(URL);
		}catch(NullPointerException npe){
			this.url = getActivity().getIntent().getStringExtra(URL);
		}
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		progressBar = (ProgressBar) this.getView().findViewById(R.id.webProgressBar);
		webView = (WebView) this.getView().findViewById(R.id.webView);
		progressBar.getProgressDrawable().setColorFilter(Color.GRAY, Mode.SRC_IN);
		webView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN){
					switch(keyCode){
					case KeyEvent.KEYCODE_BACK:
						if(webView.canGoBack()){
							webView.goBack();
							return true;
						}
						break;
					}
				}
				return false;
			}
		});
		loadPage();
	}

	public void loadPage(){
		
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				try{
					progressBar.setProgress(newProgress);
				}catch(Exception e){
					
				}
			}
			
		});
		webView.setWebViewClient(new WebViewClient(){

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
			}
			
		}); 
		webView.loadUrl(this.url);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(!isVisibleToUser && webView!=null && url!=null && !url.equals(webView.getUrl())){
			webView.loadUrl(url);
		}
	}

}
