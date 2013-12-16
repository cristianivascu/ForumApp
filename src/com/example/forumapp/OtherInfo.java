package com.example.forumapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/*class used to display the content of the Other Info section*/
public class OtherInfo extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String LOG_TAG = "OtherInfo";
		Log.i(LOG_TAG," activity");
		
		setContentView(R.layout.otherinfo);

	}
}