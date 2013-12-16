package com.example.forumapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

/*class used to display the About/Help section*/
public class Help extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String LOG_TAG = "Help";
		Log.i(LOG_TAG," activity");
		
		/*all the information is contained in the about_help.html file; 
		 * creates a webview to display the page*/
		WebView webview = new WebView(Help.this);
		webview.loadUrl("file:///android_asset/about_help.html");
		setContentView(webview);
	}

}