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

/*this class provides the functionality of the list of cafes and bars. It is almost identical to 
 *Eat.java and Sleep.java, and all the comments are in Eat.java. The only differences are the values
 *of some strings, to match the type of the list. A future version of this application
 *will merge the three classes together*/
public class Drink extends Activity {

	public static final String PREF_FILE_NAME = "PrefFile2";
	public static final String PLACE_TYPE = "Drink";
	String deleted;
	String extraEntries;
	int filterDistance;
	int filterRating;
	String filterType;
	String[] someTypes = { "Bar", "Cafe", "Pub" };
	int currentid;
	CharSequence placenm;

	public SharedPreferences preferences;// =
											// getSharedPreferences(PREF_FILE_NAME,0);
	public SharedPreferences.Editor editor;// = preferences.edit();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String LOG_TAG = "Drink";
		Log.i(LOG_TAG," class");
		
		preferences = getSharedPreferences(PREF_FILE_NAME, 0);
		editor = preferences.edit();

		final Context context = Drink.this;

		ListView ls2 = new ListView(context);

		// clear previous results in the LV
		ls2.setAdapter(null);
		// populate
		final ArrayList<Place> m_Places = new ArrayList<Place>();
		Place place;

		final ArrayList<String[]> barsAndCafes = new ArrayList<String[]>();
		String[][] toAdd = {
				{ "0", "Kilimanjaro Coffee", "Cafe",
						"104 Nicolson St., EH8 9EJ", "5", "0131 663 0135",
						"thecafehunter.co.uk" },
				{ "1", "Beetlejuice", "Cafe", "33 West Nicolson St., EH8 9DB",
						"5", "0131 668 3824", "beetlejuicebar.co.uk" },
				{ "2", "Beanscene", "Cafe", "99 Nicolson St., EH8 9BY", "5",
						"0131 667 5697", "beanscene.co.uk" },
				{ "3", "Elephants & Bagels", "Cafe",
						"37 Marshall St., EH8 9BJ", "5", " 0131 668 4404",
						"elephanthouse.biz" },
				{ "4", "The Elephant House", "Cafe",
						"25 George IV Bridge, EH1 1EP", "10", "0131 220 5355",
						"elephanthouse.biz" },
				{ "5", "56 North", "Bar", "2-8 West Crosscauseway, EH8 9JP",
						"5", "0131 662 8860", "fiftysixnorth.co.uk" },
				{ "6", "The Counting House", "Bar",
						"36 West Nicolson St., EH8 9DD", "5", "0131 667 7533",
						"counting-house.co.uk" },
				{ "7", "The Southsider", "Bar", "3/5/7 West Richmond, EH8 9EF",
						"5", "0131 510 0051", "southsidemanagement.com" },
				{ "8", "The Greyfriars Bobby", "Bar",
						"30-34 Candlemaker Row, EH1 2QE", "10",
						"0131 225 8328", "nicholsonpubs.co.uk" },
				{ "9", "Malones Irish Bar", "Bar", "14 Forrest Road, EH1 2QN",
						"10", "0131 226 5954", "malonesedinburgh.com" },
				{ "10", "Starbucks Coffee Co", "Cafe", "140 Nicolson St., EH8 9EH",
						"10", "0131 662 8947", "starbucks.co.uk" },
				{ "11", "Teviot Row House", "Pub", "13 Bristo Square, EH8 9AJ",
						"5", "0131 650 4673", "eusa.ed.ac.uk" }

		};

		for (String[] entry : toAdd) {
			barsAndCafes.add(entry);
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
					barsAndCafes.add(tokens);
				}
			}

		}

		

		deleted = preferences.getString("deletedId", "_");
		filterDistance = preferences.getInt("distanceFilter", 0);
		filterRating = preferences.getInt("ratingFilter", 0);
		filterType = preferences.getString("typeFilter", "Clear filter");

		for (int i = 0; i < barsAndCafes.size(); i++) {
			int id = Integer.parseInt(barsAndCafes.get(i)[0]);
			String nm = barsAndCafes.get(i)[1];
			String typ = barsAndCafes.get(i)[2];
			String addr = barsAndCafes.get(i)[3];
			String dst = barsAndCafes.get(i)[4];
			int rt = giveMeRatingOf(id);
			String tel = barsAndCafes.get(i)[5];
			String web = barsAndCafes.get(i)[6];
			boolean notDeleted = true;
			boolean okForDistance = true;
			boolean okForRating = true;
			boolean okForType = true;

			if (deleted.contains("_" + barsAndCafes.get(i)[0] + "_")) {
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

			if (!filterType.equals("Clear filter") && !filterType.equals(typ)
					&& !typ.equals("N/A")) {
				okForType = false;
			}

			boolean inKnownTypes = false;
			for (String cs : someTypes) {
				if (typ.equals(cs))
					inKnownTypes = true;
			}

			if (filterType.equals("Other") && !inKnownTypes) {
				okForType = true;
			}
		

			if (notDeleted && okForDistance && okForRating && okForType) {
				place = new Place(id, nm, typ, addr, dst, rt, tel, web,
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
						
							AlertDialog.Builder builder = new AlertDialog.Builder(
									Drink.this);
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

													Intent drink = new Intent(
															Drink.this,
															Drink.class);

													Toast.makeText(
															getBaseContext(),
															"Deleted "
																	+ placenm,
															Toast.LENGTH_SHORT)
															.show();

													
													startActivity(drink);
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
									Drink.this);
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
											Intent drink = new Intent(
													Drink.this, Drink.class);
											startActivity(drink);
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

	// takes the id of the place and returns the corresponding rating
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
			View layout = inflater.inflate(R.layout.userinputdrink,
					(ViewGroup) findViewById(R.id.promptdrink));

			final EditText nameField = (EditText) layout
					.findViewById(R.id.inputdrink1);
			final EditText distanceField = (EditText) layout
					.findViewById(R.id.inputdrink2);
			final EditText typeField = (EditText) layout
					.findViewById(R.id.inputdrink3);
			final EditText addressField = (EditText) layout
					.findViewById(R.id.inputdrink4);
			final EditText phoneField = (EditText) layout
					.findViewById(R.id.inputdrink5);
			final EditText websiteField = (EditText) layout
					.findViewById(R.id.inputdrink6);

			alert.setView(layout);

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							

							String name = nameField.getText().toString().trim();
							String distance = distanceField.getText()
									.toString();
							String type = typeField.getText().toString();
							String address = addressField.getText().toString();
							String phone = phoneField.getText().toString();
							String website = websiteField.getText().toString();

							// safety procedures to ensure that no fields will
							// be empty

							if (distance.isEmpty())
								distance = "N/A";
							if (type.isEmpty())
								type = "N/A";
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

							type = type.replace('_', '?');
							type = type.replace('|', '?');

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
										+ type
										+ "_"
										+ address
										+ "_"
										+ distance
										+ "_"
										+ phone
										+ "_"
										+ website + "|";

								
								editor.putString("addedEntries", newEntry); // value
																			// to
																			// store
								editor.commit();

								Toast.makeText(getBaseContext(),
										"Added: " + name, Toast.LENGTH_SHORT)
										.show();

								Intent drink = new Intent(Drink.this,
										Drink.class);
								startActivity(drink);
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

			final CharSequence[] items = { "Distance", "Your rating", "Type",
					"Clear all filters" };

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select filter category or clear all");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					
					if (item == 0) {
						final CharSequence[] items = { "Clear filter",
								"5 min or less", "10 min or less" };

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Drink.this);
						builder.setTitle("Choose maximum distance or clear filter");
						builder.setSingleChoiceItems(items, -1,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										editor.putInt("distanceFilter", item);
										editor.commit();
										dialog.dismiss();
										Intent drink = new Intent(Drink.this,
												Drink.class);
										startActivity(drink);
										finish();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();

					} else if (item == 1) {
						final CharSequence[] items = { "Clear filter",
								"5 stars", "4 stars or more", "3 stars or more" };

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Drink.this);
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
										Intent drink = new Intent(Drink.this,
												Drink.class);
										startActivity(drink);
										finish();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();

					} else if (item == 2) {
						final CharSequence[] items = { "Clear filter", "Cafe",
								"Bar", "Pub", "Other" };

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Drink.this);
						builder.setTitle("Choose a type or clear filter");
						builder.setSingleChoiceItems(items, -1,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										editor.putString("typeFilter",
												(String) items[item]); // value
																		// to
																		// store
										editor.commit();
										dialog.dismiss();
										Intent drink = new Intent(Drink.this,
												Drink.class);
										startActivity(drink);
										finish();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();
					}

					else {

						editor.putInt("distanceFilter", 0);

						editor.putInt("ratingFilter", 0);

						editor.putString("typeFilter", "Clear filter");
						editor.commit();

						Intent drink = new Intent(Drink.this, Drink.class);
						startActivity(drink);
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
			Intent drink = new Intent(Drink.this, Drink.class);
			startActivity(drink);
			finish();
			return true;

		case R.id.map:
			Bundle bundle = new Bundle();
			bundle.putInt("param", 2);
			Intent map = new Intent(this, Map.class);
			map.putExtras(bundle);
			startActivity(map);
			return true;

		}

		return false;
	}
}