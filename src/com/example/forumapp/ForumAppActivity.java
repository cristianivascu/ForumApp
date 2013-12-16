package com.example.forumapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/*the class used to display the main menu*/
public class ForumAppActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String LOG_TAG = "ForumAppActivity";
		Log.i(LOG_TAG," activity");
		
		/* Set up click listeners for all the buttons*/
		View directionsButton = findViewById(R.id.directions_button);
		directionsButton.setOnClickListener(this);
		View eatButton = findViewById(R.id.eat_button);
		eatButton.setOnClickListener(this);
		View sleepButton = findViewById(R.id.sleep_button);
		sleepButton.setOnClickListener(this);
		View otherinfoButton = findViewById(R.id.otherinfo_button);
		otherinfoButton.setOnClickListener(this);

	}
   
	/*launch activities depending on which button has been pressed; the order of the cases is not 
	 * the same as in the actual menu*/
	public void onClick(View v) {
		switch (v.getId()) {
		
		/*pressing the Other Info button*/
		case R.id.otherinfo_button:
			Intent otherinfo = new Intent(this, OtherInfo.class);
			startActivity(otherinfo);
			break;

	    /*pressing the Directions button*/
		case R.id.directions_button:
			Intent directions = new Intent(this, Directions.class);
			startActivity(directions);
			break;

		/*pressing the Eat & Drink button*/
		case R.id.eat_button:
            
			/*open an alert dialog to let the user choose between Restaurants and Bars/Cafes
			 * an then launch the corresponding activity*/
			final CharSequence[] items = { "Restaurants", "Bars & Cafes" };

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Please choose a category");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					if (item == 0) {
						Intent eat = new Intent(ForumAppActivity.this,
								Eat.class);
						startActivity(eat);
					} else {
						Intent eat = new Intent(ForumAppActivity.this,
								Drink.class);
						startActivity(eat);
					}
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

			break;
        
		/*pressing the Places to Sleep button*/
		case R.id.sleep_button:
			Intent sleep = new Intent(this, Sleep.class);
			startActivity(sleep);
			break;

		}

	}

	/*create an options menu*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_for_main, menu);
		return true;
	}

	/*will have two options: About/Help and  Factory Reset*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		/*launch the help activity*/
		case R.id.help:

			Intent help = new Intent(this, Help.class);
			startActivity(help);
			return true;

		/*perform a factory reset by reinitializing all the variables saved in the preference files 
		 *(which save the state of the application). This option will return all the lists to their 
		 *initial state, clearing filters, added entries and ratings, and restoring deleted entries*/
		case R.id.reset:
            /*alert dialog asking for confirmation*/
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Factory reset will bring your application to its inital state. Any added entries, ratings, or other options will be lost! Are you sure you want to proceed?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									/*reinitialize variables*/

									SharedPreferences preferences = getSharedPreferences(
											"PrefFile", 0);
									SharedPreferences.Editor editor = preferences
											.edit();

									editor.putString("deletedId", "_");
									editor.putString("ratings", "_");
									editor.putString("addedEntries", "|");
									editor.putInt("nextid", 12);
									editor.putInt("distanceFilter", 0);
									editor.putInt("ratingFilter", 0);
									editor.putString("cuisineFilter",
											"Clear filter");
									editor.commit();

									SharedPreferences preferences2 = getSharedPreferences(
											"PrefFile2", 0);
									SharedPreferences.Editor editor2 = preferences2
											.edit();

									editor2.putString("deletedId", "_");
									editor2.putString("ratings", "_");
									editor2.putString("addedEntries", "|");
									editor2.putInt("nextid", 12);
									editor2.putInt("distanceFilter", 0);
									editor2.putInt("ratingFilter", 0);
									editor2.putString("typeFilter",
											"Clear filter");
									editor2.commit();

									SharedPreferences preferences3 = getSharedPreferences(
											"PrefFile3", 0);
									SharedPreferences.Editor editor3 = preferences3
											.edit();

									editor3.putString("deletedId", "_");
									editor3.putString("ratings", "_");
									editor3.putString("addedEntries", "|");
									editor3.putInt("nextid", 12);
									editor3.putInt("distanceFilter", 0);
									editor3.putInt("ratingFilter", 0);
									editor3.putString("ratCatFilter",
											"Clear filter");
									editor3.commit();

									Toast.makeText(getBaseContext(),
											"Restored to initial settings",
											Toast.LENGTH_SHORT).show();

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

			return true;

		}

		return false;
	}

}