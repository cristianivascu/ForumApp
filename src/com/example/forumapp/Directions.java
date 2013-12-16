package com.example.forumapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

/*this class is used to display the directions to the Forum*/
public class Directions extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String LOG_TAG = "Directions";
		Log.i(LOG_TAG," activity");
		/*all the information is contained in the directions_text.html file; creates
		 * a webview to display the page*/
		WebView webview = new WebView(Directions.this);
		webview.loadUrl("file:///android_asset/directions_text.html");
		setContentView(webview);
	}

	/*create an options menu*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_directions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		/*there is only one item in the menu, allowing to select the map with key locations*/
		case R.id.directionsmap:

			/*the Map class will be used to display the map; several maps are used in 
			 *this application, and "param" stores the id of the map we want to open  */
			Bundle bundle = new Bundle();
			bundle.putInt("param", 0);
            Intent map = new Intent(this, Map.class);
			map.putExtras(bundle);
			startActivity(map);
			return true;

		}

		return false;
	}

}