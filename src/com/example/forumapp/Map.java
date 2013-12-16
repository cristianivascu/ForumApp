package com.example.forumapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/*a simple class used to display a map*/
public class Map extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String LOG_TAG = "Map";
		Log.i(LOG_TAG," activity");
		
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bundle bundle = this.getIntent().getExtras();
		int param = bundle.getInt("param", -1);

		/*check the bundled "param" id to 'decide' which map to open*/
		if (param == 0) 
			setContentView(R.layout.mapdirections);
		else if (param == 1)
			setContentView(R.layout.mapeat);
		else if (param == 2)
			setContentView(R.layout.mapdrink);
		else
			setContentView(R.layout.mapsleep);
	}
}