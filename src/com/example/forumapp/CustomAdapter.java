package com.example.forumapp;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

/*this class will use the CustomAdapterView private class and implement the getView method*/
public class CustomAdapter extends BaseAdapter {

	public static final String LOG_TAG = "CustomAdapter";
	private Context context;
	private List<Place> placeList;

	public CustomAdapter(Context context, List<Place> placeList) {
		this.context = context;
		this.placeList = placeList;
	}

	public int getCount() {
		return placeList.size();
	}

	public Object getItem(int position) {
		return placeList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Place place = placeList.get(position);
		View v = new CustomAdapterView(this.context, place);

		v.setBackgroundColor((position % 2) == 1 ? Color.GRAY : Color.BLACK);

		return v;
	}

}
