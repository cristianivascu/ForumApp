package com.example.forumapp;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.Toast;

/*this class provides the functionality of the list of hotels. It is almost identical to 
*Eat.java and Drink.java, and all the comments are in Eat.java. The only differences are the values
*of some strings, to match the type of the list. A future version of this application
*will merge the three classes together*/
public class Sleep extends Activity {

	public static final String PREF_FILE_NAME = "PrefFile3";
	public static final String PLACE_TYPE = "Sleep";

	String deleted;
	String extraEntries;
	int filterDistance;
	int filterRating;
	String filterRatAndCat;
	int currentid;
	CharSequence placenm;

	public SharedPreferences preferences;
	public SharedPreferences.Editor editor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String LOG_TAG = "Sleep";
		Log.i(LOG_TAG," activity");
		
		preferences = getSharedPreferences(PREF_FILE_NAME, 0);
		editor = preferences.edit();

		final Context context = Sleep.this;

		ListView ls2 = new ListView(context);

		// clear previous results in the LV
		ls2.setAdapter(null);
		// populate
		final ArrayList<Place> m_Places = new ArrayList<Place>();
		Place place;

		final ArrayList<String[]> hotels = new ArrayList<String[]>();
		String[][] toAdd = {
				{ "0", "Nicolson Apartments", "4-star, self-catered",
						"28 West Nicolson St., EH8 9DD", "5", "0131 477 3680",
						"apts-edinburgh.co.uk" },
				{ "1", "Ten Hill Place Hotel", "4-star, hotel",
						"10 Hill Place, EH8 9DS", "10", "0131 662 2080",
						"tenhillplace.com" },
				{ "2", "Hotel du Vin", "4-star, hotel",
						"11 Bristo Place, EH1 1EZ", "10", "0131 247 4900",
						"hotelduvin.com" },
				{ "3", "Ibis Edinburgh Centre", "2-star, hotel",
						"6 Hunter Square, EH1 1QW", "15", " 0131 240 700",
						"ibishotel.com" },
				{ "4", "The Bank Hotel", "3-star, hotel",
						"1 South Bridge, EH1 1LL", "15", "0131 556 9940",
						"festival-inns.co.uk" },
				{ "5", "Euro Hostel", "3-star, hostel",
						"4/2 Kincaids Court, EH1 1JT", "10", "0845 490 0461",
						"euro-hostels.co.uk" },
				{ "6", "Hotel Missoni", "5-star, hotel",
						"1 George IV Bridge, EH1 1AD", "15", "0131 220 6666",
						"hotelmissoni.com" },
				{ "7", "Kenneth MacKenzie Suite", "3-star, B&B",
						"7 Richmond Place, EH8 9ST", "10", "0131 651 2007",
						"edinburghfirst.co.uk" },
				{ "8", "Smart City Hostel", "5-star, hostel",
						"50 Blackfriars St., EH1 1NE", "15", "0131 524 1989",
						"smartcityhostels.com" },
				{ "9", "Quartermile Apartments", "4-star, self-catered",
						"28 Simpson Loan, EH3 9GG", "10", "0131 343 2239",
						"qsapartments.co.uk" },
				{ "10", "Radisson Blu", "4-star, hotel",
						"80 High Street, EH1 1TH", "15", "0131 473 6590",
						"radissonblu.co.uk" },
				{ "11", "Metro Hostel", "3-star, hostel",
						"11/2 Robertson's Close, EH1 1LY", "15", "0131 524 2090",
						"syha.org.uk" }
						
		};

		for (String[] entry : toAdd) {
			hotels.add(entry);
		}

		// lots of parsing

		extraEntries = preferences.getString("addedEntries", "|");

		String delims1 = "[|]";
		String[] extraEntriesArray = extraEntries.split(delims1);
		if (extraEntriesArray.length != 0) {
			for (String entry : extraEntriesArray) {
				String delims2 = "[_]";
				String[] tokens = entry.split(delims2);
				if (tokens.length > 2) {
					hotels.add(tokens);
				}
			}

		}

		deleted = preferences.getString("deletedId", "_");
		filterDistance = preferences.getInt("distanceFilter", 0);
		filterRating = preferences.getInt("ratingFilter", 0);
		filterRatAndCat = preferences.getString("ratCatFilter", "Clear filter");

		for (int i = 0; i < hotels.size(); i++) {
			int id = Integer.parseInt(hotels.get(i)[0]);
			String nm = hotels.get(i)[1];
			String ratAndCat = hotels.get(i)[2];
			String addr = hotels.get(i)[3];
			String dst = hotels.get(i)[4];
			int rt = giveMeRatingOf(id);
			String tel = hotels.get(i)[5];
			String web = hotels.get(i)[6];
			boolean notDeleted = true;
			boolean okForDistance = true;
			boolean okForRating = true;
			boolean okForRatAndCat = true;

			if (deleted.contains("_" + hotels.get(i)[0] + "_")) {
				notDeleted = false;
			}

			if (isParsableToInt(dst)) {

				if ((filterDistance == 1 && Integer.parseInt(dst) > 5)
						|| (filterDistance == 2 && Integer.parseInt(dst) > 10)) {
					okForDistance = false;
				}
			}
			if (rt != 0) {
				if ((filterRating == 1 && rt < 5)
						|| (filterRating == 2 && rt < 4)
						|| (filterRating == 3 && rt < 3)) {
					okForRating = false;
				}
			}

			if (!filterRatAndCat.equals("Clear filter")
					&& !ratAndCat.startsWith(filterRatAndCat)
					&& !ratAndCat.startsWith("N/A")) {
				okForRatAndCat = false;
			}

			if (notDeleted && okForDistance && okForRating && okForRatAndCat) {
				place = new Place(id, nm, ratAndCat, addr, dst, rt, tel, web,
						PLACE_TYPE);
				m_Places.add(place);
			}
		}

		if (m_Places.isEmpty()) {
			Toast.makeText(getBaseContext(), "No Entries... ",
					Toast.LENGTH_LONG).show();
		}

		final CharSequence[] items = { "Rate place", "Delete place" };

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose an option");

		CustomAdapter lvAdapter = new CustomAdapter(context, m_Places);
		ls2.setAdapter(lvAdapter);
		ls2.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				currentid = arg1.getId();// arg2;
				placenm = arg1.getContentDescription();

				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 1) {
							/*
							 * SharedPreferences preferences =
							 * getSharedPreferences(PREF_FILE_NAME, 0);
							 */

							AlertDialog.Builder builder = new AlertDialog.Builder(
									Sleep.this);
							builder.setMessage(
									"Are you sure you want to delete "
											+ placenm + "?")
									.setCancelable(false)
									.setPositiveButton(
											"Yes",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {

													deleted = preferences
															.getString(
																	"deletedId",
																	"_")
															+ currentid + "_";

													editor.putString(
															"deletedId",
															deleted); // value
																		// to
																		// store
													editor.commit();

													Intent sleep = new Intent(
															Sleep.this,
															Sleep.class);

													Toast.makeText(
															getBaseContext(),
															"Deleted "
																	+ placenm,
															Toast.LENGTH_SHORT)
															.show();

													startActivity(sleep);
													finish();

												}
											})
									.setNegativeButton(
											"No",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.cancel();
												}
											});
							AlertDialog alert = builder.create();
							alert.show();

						} else { // if (item == 0){

							final CharSequence[] items = { "1* Poor",
									"2* Acceptable", "3* Good", "4* Very Good",
									"5* Excellent" };

							AlertDialog.Builder builder = new AlertDialog.Builder(
									Sleep.this);
							builder.setTitle("Choose a rating:");
							builder.setSingleChoiceItems(items, -1,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int item) {
											int rtng = item + 1;
											String ratings_and_ids;
											ratings_and_ids = preferences
													.getString("ratings", "_")
													+ rtng
													+ ","
													+ currentid
													+ "_";
											editor.putString("ratings",
													ratings_and_ids);
											editor.commit();
											Toast.makeText(
													getBaseContext(),
													"You gave a "
															+ rtng
															+ "-star rating to "
															+ placenm,
													Toast.LENGTH_LONG).show();
											dialog.dismiss();
											Intent sleep = new Intent(
													Sleep.this, Sleep.class);
											startActivity(sleep);
											finish();

										}
									});
							AlertDialog alert = builder.create();
							alert.show();

						}

					}
				});
				AlertDialog alert = builder.create();
				alert.show();

			}
		});

		setContentView(ls2);
	}

	// takes the id of the restaurant and returns the corresponding rating
	public int giveMeRatingOf(int id) {
		int rating = 0;
		String rat = preferences.getString("ratings", "_");
		// lots of parsing
		String delims = "[_]";
		String[] tokens = rat.split(delims);
		if (tokens.length != 0) {

			for (String token : tokens) {

				String delims2 = "[,]";
				String[] tuple = token.split(delims2);
				if (tuple.length == 2) {
					if (Integer.parseInt(tuple[1]) == id) {
						rating = Integer.parseInt(tuple[0]);
					}
				}

			}

		}

		return rating;

	}

	public boolean isParsableToInt(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_place, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addentry:

			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Add a new place");
		
			Context mContext = getApplicationContext();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.userinputsleep,
					(ViewGroup) findViewById(R.id.promptsleep));

			final EditText nameField = (EditText) layout
					.findViewById(R.id.inputsleep1);
			final EditText distanceField = (EditText) layout
					.findViewById(R.id.inputsleep2);
			final EditText stbRatField = (EditText) layout
					.findViewById(R.id.inputsleep3);
			final EditText catField = (EditText) layout
					.findViewById(R.id.inputsleep4);
			final EditText addressField = (EditText) layout
					.findViewById(R.id.inputsleep5);
			final EditText phoneField = (EditText) layout
					.findViewById(R.id.inputsleep6);
			final EditText websiteField = (EditText) layout
					.findViewById(R.id.inputsleep7);

			alert.setView(layout);

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							String name = nameField.getText().toString().trim();
							String distance = distanceField.getText()
									.toString();
							String stbrat = stbRatField.getText().toString();
							String cat = catField.getText().toString();

							if (stbrat.isEmpty())// safety procedures
								stbrat = "N/A";
							if (cat.isEmpty())
								cat = "N/A";

							String stbratAndCat = stbrat + "-star, " + cat;
							String address = addressField.getText().toString();
							String phone = phoneField.getText().toString();
							String website = websiteField.getText().toString();

							// safety procedures to ensure that no fields will
							// be empty

							if (distance.isEmpty())
								distance = "N/A";
							if (address.isEmpty())
								address = "N/A";
							if (phone.isEmpty())
								phone = "N/A";
							if (website.isEmpty())
								website = "N/A";

							// safety procedures to ensure that no characters
							// used for parsing (| or _) are present

							name = name.replace('_', '?');
							name = name.replace('|', '?');

							distance = distance.replace('_', '?');
							distance = distance.replace('|', '?');

							stbratAndCat = stbratAndCat.replace('_', '?');
							stbratAndCat = stbratAndCat.replace('|', '?');

							address = address.replace('_', '?');
							address = address.replace('|', '?');

							phone = phone.replace('_', '?');
							phone = phone.replace('|', '?');

							website = website.replace('_', '?');
							website = website.replace('|', '?');

							if (!name.isEmpty()) {

								int idnext = preferences.getInt("nextid", 12) + 1;
							
								editor.putInt("nextid", idnext); // value to
																	// store
								editor.commit();
							

								String newEntry = preferences.getString(
										"addedEntries", "|")
										+ idnext
										+ "_"
										+ name
										+ "_"
										+ stbratAndCat
										+ "_"
										+ address
										+ "_"
										+ distance
										+ "_"
										+ phone + "_" + website + "|";

							
								editor.putString("addedEntries", newEntry); // value
																			// to
																			// store
								editor.commit();

								Toast.makeText(getBaseContext(),
										"Added: " + name, Toast.LENGTH_SHORT)
										.show();

								Intent sleep = new Intent(Sleep.this,
										Sleep.class);
								startActivity(sleep);
								finish();
							}

							else {
								Toast.makeText(getBaseContext(),
										"Add at least a NAME for your place!",
										Toast.LENGTH_LONG).show();
							}

						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						
						}
					});

			alert.show();

			return true;

		case R.id.filterentries:

			final CharSequence[] items = { "Distance", "Your rating",
					"STB rating", "Clear all filters" };

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select filter category or clear all");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					
					if (item == 0) {
						final CharSequence[] items = { "Clear filter",
								"5 min or less", "10 min or less" };

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Sleep.this);
						builder.setTitle("Choose maximum distance or clear filter");
						builder.setSingleChoiceItems(items, -1,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										editor.putInt("distanceFilter", item);
										editor.commit();
										dialog.dismiss();
										Intent sleep = new Intent(Sleep.this,
												Sleep.class);
										startActivity(sleep);
										finish();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();

					} else if (item == 1) {
						final CharSequence[] items = { "Clear filter",
								"5 stars", "4 stars or more", "3 stars or more" };

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Sleep.this);
						builder.setTitle("Choose minimum rating or clear filter");
						builder.setSingleChoiceItems(items, -1,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										editor.putInt("ratingFilter", item); // value
																				// to
																				// store
										editor.commit();
										dialog.dismiss();
										Intent sleep = new Intent(Sleep.this,
												Sleep.class);
										startActivity(sleep);
										finish();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();

					} else if (item == 2) {
						final CharSequence[] items = { "Clear filter",
								"5-star", "4-star", "3-star", "2-star",
								"1-star" };

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Sleep.this);
						builder.setTitle("Choose a STB rating or clear filter");
						builder.setSingleChoiceItems(items, -1,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										editor.putString("ratCatFilter",
												(String) items[item]); // value
																		// to
																		// store
										editor.commit();
										dialog.dismiss();
										Intent sleep = new Intent(Sleep.this,
												Sleep.class);
										startActivity(sleep);
										finish();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();
					}

					else {

						editor.putInt("distanceFilter", 0);

						editor.putInt("ratingFilter", 0);

						editor.putString("ratCatFilter", "Clear filter");
						editor.commit();

						Intent sleep = new Intent(Sleep.this, Sleep.class);
						startActivity(sleep);
						finish();

					}

				}
			});
			AlertDialog alert2 = builder.create();
			alert2.show();
			return true;

		case R.id.restore:

			editor.putString("deletedId", "_");
			editor.commit();
			Toast.makeText(getBaseContext(), "Restored", Toast.LENGTH_SHORT)
					.show();
			Intent sleep = new Intent(Sleep.this, Sleep.class);
			startActivity(sleep);
			finish();
			return true;

		case R.id.map:

			Bundle bundle = new Bundle();
			bundle.putInt("param", 3);
			Intent map = new Intent(this, Map.class);
			map.putExtras(bundle);
			startActivity(map);
			return true;

		}

		return false;
	}
}