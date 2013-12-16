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

/*this class provides the functionality of the list of restaurants. Drink.java and Sleep.java are
 *almost identical and therefore will not include comments. A future version of this application
 *will merge the three classes together*/
public class Eat extends Activity {
    /*define the name of the preference file and the type of place("Eat" denotes restaurants)*/
	public static final String PREF_FILE_NAME = "PrefFile";
	public static final String PLACE_TYPE = "Eat";
    
	/*these strings will store the deleted IDs and added entries respectively*/
	String deleted;
	String extraEntries;
	/*the variables below, will store the conditions for filtering*/
	int filterDistance;
	int filterRating;
	String filterCuisine;
	/*an array of some possible cuisines*/
	String[] someCuisines = { "British", "Chinese", "French", "German",
			"Greek", "Indian", "Italian", "Japanese", "Lebanese", "Mexican","Fast-food" };
	/*the id of the corresponding place and its name*/
	int currentid;
	CharSequence placenm;
    
	/*declare the shared preferences interface and an editor*/
	public SharedPreferences preferences;
	public SharedPreferences.Editor editor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String LOG_TAG = "Eat";
		Log.i(LOG_TAG," activity");
        
		/*access the preferences file and update the editor*/
		preferences = getSharedPreferences(PREF_FILE_NAME, 0);
		editor = preferences.edit();

		final Context context = Eat.this;
        
		/*create a listview*/
		ListView ls2 = new ListView(context);

		/*clear previous results in the LV*/
		ls2.setAdapter(null);
		/*create and arrayList holding place objects*/
		final ArrayList<Place> m_Places = new ArrayList<Place>();
		Place place;

		/*create an arrayList holding arrays of strings; each such array will denote a place and its
		 *corresponding description*/
		final ArrayList<String[]> restaurants = new ArrayList<String[]>();
		/*create a matrix of string-arrays; each row will be a string array denoting the place and 
		 *its description; the entries below are provided as standard. More can be provided, but in 
		 *this case nextId (declared below in the class) has to be modified */
		String[][] toAdd = {
				{ "0", "Pink Olive", "British",
						"55-57 West Nicolson St., EH8 9DB", "5",
						"0131 662 4493", "ilovepinkolive.co.uk" },
				{ "1", "Khushi's Diner", "Indian",
						"38B West Nicolson St., EH8 9DD", "5", "0131 667 4871",
						"khushisdiner.com" },
				{ "2", "Ciao Roma", "Italian", "64 South Bridge, EH1 1LS",
						"10", "0131 557 3777", "ciaoroma.co.uk" },
				{ "3", "Suruchi", "Indian", "14a Nicolson St., EH8 9DH", "10",
						" 0131 556 6583", "suruchirestaurant.com" },
				{ "4", "Petit Paris", "French", "38-40 Grassmarket, EH1 2JU",
						"15", "0131 226 2442", "petitparis-restaurant.co.uk" },
				{ "5", "Red-Box", "Chinese",
						"51-53 West Nicolson St., EH8 9DB", "5",
						"0131 662 0828", "red-boxnoodlebar.co.uk" },
				{ "6", "Beirut Restaurant", "Lebanese",
						"14 Marshall St., EH8 9BU", "5", "0131 667 9919",
						"beirutrestaurant.co.uk" },
				{ "7", "The Royal McGregor", "British",
						"154 High Street, EH1 1QS", "15", "0131 225 7064",
						"royalmcgregor.co.uk" },
				{ "8", "City Restaurant", "Various",
						"35 Nicolson St., EH8 9BE", "10", "0131 667 2819",
						"thecityrestaurant.co.uk" },
				{ "9", "The Tower", "British",
						"National Museum of Scotland,Chambers St., EH1 1JF",
						"10", "0131 225 3003", "tower-restaurant.com" }, 
				{ "10", "Mariachi", "Mexican",
						"7 Victoria St., EH1 1HE",
						"15", "0131 623 0077", "mariachi-restaurant.co.uk" },
				{ "11", "KFC", "Fast-food",
						"36 Nicolson St., EH8 9DT",
						"5", "0131 662 9533", "kfc.co.uk" }		
				        };
        
		/*add each row of the toAdd matrix to the restaurants arrayList*/
		for (String[] entry : toAdd) {
			restaurants.add(entry);
		}

		/*addedEntries variable of the preference file will store entries added by the user. This is 
		 *a large string which requires parsing*/
		
		extraEntries = preferences.getString("addedEntries", "|");
        /*first separate the entries*/
		String delims1 = "[|]";
		String[] extraEntriesArray = extraEntries.split(delims1);
		if (extraEntriesArray.length != 0) {
			/*then for each entry, separate the elements of the description (i.e, id, name, cuisine, etc)*/
			for (String entry : extraEntriesArray) {
				String delims2 = "[_]";
				String[] tokens = entry.split(delims2);
				/*a string-array will be obtained denoting an entry; update the restaurants arrayList*/
				if (tokens.length > 2) {
					restaurants.add(tokens);
				}
			}

		}
        /*update the deleted IDs and the filters*/
		deleted = preferences.getString("deletedId", "_");
		filterDistance = preferences.getInt("distanceFilter", 0);
		filterRating = preferences.getInt("ratingFilter", 0);
		filterCuisine = preferences.getString("cuisineFilter", "Clear filter");
        
		/*this large for loop will eventually create place-objects and will update the m_Places arrayList*/
		/*iterate through the restaurants list*/
		for (int i = 0; i < restaurants.size(); i++) {
			/*for each entry, update the elements of the description*/
			int id = Integer.parseInt(restaurants.get(i)[0]);
			String nm = restaurants.get(i)[1];
			String cuisn = restaurants.get(i)[2];
			String addr = restaurants.get(i)[3];
			String dst = restaurants.get(i)[4];
			int rt = giveMeRatingOf(id);
			String tel = restaurants.get(i)[5];
			String web = restaurants.get(i)[6];
			
			/*these variables will be used to decide which entries go in the m_Places list and which not;
			 *initialize them, assuming there are no deleted entries and no filters have been applied */
			boolean notDeleted = true;
			boolean okForDistance = true;
			boolean okForRating = true;
			boolean okForCuisine = true;

			/*if the id of the current place is in the deleted string, update notDeleted;
			 *the place will not be added to the m_Places list*/
			if (deleted.contains("_" + restaurants.get(i)[0] + "_")) {
				notDeleted = false;
			}

			
			if (isParsableToInt(dst)) {
                /*if a filter distance has been selected, filterDistance will be updated.
                 *value 1 is the condition of places being 5 min or closer, and value 2 is for 10 min;
                 *value 0 corresponds to Clear filter so the boolean should remain true*/
				if ((filterDistance == 1 && Integer.parseInt(dst) > 5)
						|| (filterDistance == 2 && Integer.parseInt(dst) > 10)) {
					okForDistance = false;
				}
			}
			
			/*the filter for rating works in similar manner; please note that if no rating is available,
			 *the filter will be ignored and the entry will still be displayed; this is the working 
			 *principle in general for all the filters*/
			if (rt != 0) {
				if ((filterRating == 1 && rt < 5)
						|| (filterRating == 2 && rt < 4)
						|| (filterRating == 3 && rt < 3)) {
					okForRating = false;
				}
			}
            
			/*check if the cuisine corresponds to the one of the filter*/
			if (!filterCuisine.equals("Clear filter")
					&& !filterCuisine.equals(cuisn) && !cuisn.equals("N/A")) {
				okForCuisine = false;
			}
 
			/*the filter offers an "Other" option so that the users can see cuisines which are
			 *not in the list; displays all the cuisines which are not in the list*/
			boolean inKnownCuisines = false;
			for (String cs : someCuisines) {
				if (cuisn.equals(cs))
					inKnownCuisines = true;
			}

			if (filterCuisine.equals("Other") && !inKnownCuisines) {
				okForCuisine = true;
			}
		
            /*check all the booleans above and update the m_Places list*/
			if (notDeleted && okForDistance && okForRating && okForCuisine) {
				place = new Place(id, nm, cuisn, addr, dst, rt, tel, web,
						PLACE_TYPE);
				m_Places.add(place);
			}
		}
		/*display a message whenever there are no entries to display*/
		if (m_Places.isEmpty()) {
			Toast.makeText(getBaseContext(), "No Entries... ",
					Toast.LENGTH_LONG).show();
		}
        /*if a user will press an item from the list, an alert dialog will appear 
         *with two options: Rate place and Delete place*/
		final CharSequence[] items = { "Rate place", "Delete place" };

		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose an option");
        /*implement click listeners*/
		CustomAdapter lvAdapter = new CustomAdapter(context, m_Places);
		ls2.setAdapter(lvAdapter);
		ls2.setOnItemClickListener(new OnItemClickListener() {
			/*action when an item is clicked*/
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
                /*store the ID and name of the pressed view (the selected entry)*/
				currentid = arg1.getId();// arg2;
				placenm = arg1.getContentDescription();

				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						/*if delete has been selected from the dialog box*/
						if (item == 1) {
						
                            /*ask for confirmation*/
							AlertDialog.Builder builder1 = new AlertDialog.Builder(
									Eat.this);
							builder1.setMessage(
									"Are you sure you want to delete "
											+ placenm + "?")
									.setCancelable(false)
									.setPositiveButton(
											"Yes",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
                                    /*if confirmed, update the string of deleted IDs*/
													deleted = preferences
															.getString(
																	"deletedId",
																	"_")
															+ currentid + "_";

										
													editor.putString(
															"deletedId",
															deleted); 
													
													editor.commit();
                      /*re-launch the activity to see the effect and print a message to confirm*/
													Intent eat = new Intent(
															Eat.this, Eat.class);

													Toast.makeText(
															getBaseContext(),
															"Deleted "
																	+ placenm,
															Toast.LENGTH_SHORT)
															.show();

													

													startActivity(eat);
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
							AlertDialog alert1 = builder1.create();
							alert1.show();

						} 
						/*if rate has been selected from the dialog box*/
						else { 

							final CharSequence[] items = { "1* Poor",
									"2* Acceptable", "3* Good", "4* Very Good",
									"5* Excellent" };
                        /*display a dialog box with the possible options*/
							AlertDialog.Builder builder2 = new AlertDialog.Builder(
									Eat.this);
							builder2.setTitle("Choose a rating:");
							builder2.setSingleChoiceItems(items, -1,
									new DialogInterface.OnClickListener() {
					  /*after an options has been chosen, update the string containing 
					   * IDs and corresponding ratings and display a confirmation message*/			
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
                        /*close the dialog and re-launch the activity to see the changes*/
											dialog.dismiss();
											Intent eat = new Intent(Eat.this,
													Eat.class);
											startActivity(eat);
											finish();

										}
									});
							AlertDialog alert2 = builder2.create();
							alert2.show();

						}

					}
				});
				AlertDialog alert = builder.create();
				alert.show();

			}
		});

		setContentView(ls2);
	}

	/* method which takes the id of the restaurant and returns the corresponding rating;
	 * it works by parsing the string containing IDs and ratings, to extract the rating of
	 * an ID */
	public int giveMeRatingOf(int id) {
		int rating = 0;
		String rat = preferences.getString("ratings", "_");
		/* lots of parsing*/
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
   /*method to check if a String can be parsed to an Int*/
	public boolean isParsableToInt(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

   /*create the Options menu*/
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
		
		/*if the user chooses to add an entry*/
		case R.id.addentry:
            /*display an alert box with the fields which should be completed*/
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Add a new place");
		

			Context mContext = getApplicationContext();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.userinputeat,
					(ViewGroup) findViewById(R.id.prompteat));

			final EditText nameField = (EditText) layout
					.findViewById(R.id.inputeat1);
			final EditText distanceField = (EditText) layout
					.findViewById(R.id.inputeat2);
			final EditText cuisineField = (EditText) layout
					.findViewById(R.id.inputeat3);
			final EditText addressField = (EditText) layout
					.findViewById(R.id.inputeat4);
			final EditText phoneField = (EditText) layout
					.findViewById(R.id.inputeat5);
			final EditText websiteField = (EditText) layout
					.findViewById(R.id.inputeat6);

			alert.setView(layout);
          
			
			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
                            /*update the fields with the information provided by the user;
                             *he is allowed to type almost anything at this point(ie. letters 
                             *instead of numbers); should be fixed at some point*/
							String name = nameField.getText().toString().trim();
							String distance = distanceField.getText()
									.toString();
							String cuisine = cuisineField.getText().toString();
							String address = addressField.getText().toString();
							String phone = phoneField.getText().toString();
							String website = websiteField.getText().toString();

			/* safety procedures to ensure that no fields will be empty; this would
			 * generate nullPointerExeptions, so a generic "N/A" will be added*/

							if (distance.isEmpty())
								distance = "N/A";
							if (cuisine.isEmpty())
								cuisine = "N/A";
							if (address.isEmpty())
								address = "N/A";
							if (phone.isEmpty())
								phone = "N/A";
							if (website.isEmpty())
								website = "N/A";

		/*safety procedures to ensure that no characters used for parsing (| or _) are present*/

							name = name.replace('_', '?');
							name = name.replace('|', '?');

							distance = distance.replace('_', '?');
							distance = distance.replace('|', '?');

							cuisine = cuisine.replace('_', '?');
							cuisine = cuisine.replace('|', '?');

							address = address.replace('_', '?');
							address = address.replace('|', '?');

							phone = phone.replace('_', '?');
							phone = phone.replace('|', '?');

							website = website.replace('_', '?');
							website = website.replace('|', '?');
         /*the user should provide at least a name;  */
							if (!name.isEmpty()) {
		/*increment the ID which will be assigned to the added entry (the ID could actually 
		 * start from 11+1 instead of 12+1, because 11 is the ID of the last provided entry; 
		 * as long as the value is higher than 11+1, everything is fine).*/
								
								int idnext = preferences.getInt("nextid", 12) + 1;
								
								editor.putInt("nextid", idnext); 
								editor.commit();
								
      /*update the string of added entries*/
								String newEntry = preferences.getString(
										"addedEntries", "|")
										+ idnext
										+ "_"
										+ name
										+ "_"
										+ cuisine
										+ "_"
										+ address
										+ "_"
										+ distance
										+ "_"
										+ phone
										+ "_"
										+ website + "|";

							
								editor.putString("addedEntries", newEntry);
								editor.commit();
                       /*confirm and relaunch the activity*/
								Toast.makeText(getBaseContext(),
										"Added: " + name, Toast.LENGTH_SHORT)
										.show();

								Intent eat = new Intent(Eat.this, Eat.class);
								startActivity(eat);
								finish();
							}

							else {
								Toast.makeText(getBaseContext(),
										"Please add at least a NAME for your place!",
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

		/*if the user chooses to filter the entries*/
		case R.id.filterentries:
            /*display an alert box with the possible options*/
			final CharSequence[] items = { "Distance", "Your rating",
					"Cuisine", "Clear all filters" };
           
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select filter category or clear all");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					/*if filter by distance is chosen, display the further options*/
					if (item == 0) {
						final CharSequence[] items = { "Clear filter",
								"5 min or less", "10 min or less" };

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Eat.this);
						builder.setTitle("Choose maximum distance or clear filter");
						builder.setSingleChoiceItems(items, -1,
								new DialogInterface.OnClickListener() {
								/*update the distanceFilter saved variable depending on the chosen option*/	
							            public void onClick(DialogInterface dialog,
											int item) {
										editor.putInt("distanceFilter", item);
										editor.commit();
										dialog.dismiss();
								/*re-launch the class to see the result*/		
										Intent eat = new Intent(Eat.this,
												Eat.class);
										startActivity(eat);
										finish();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();

					} 
					/*if filter by rating is chosen, display the further options
					 *works as above*/
					  else if (item == 1) {
						final CharSequence[] items = { "Clear filter",
								"5 stars", "4 stars or more", "3 stars or more" };

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Eat.this);
						builder.setTitle("Choose minimum rating or clear filter");
						builder.setSingleChoiceItems(items, -1,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										editor.putInt("ratingFilter", item); 
										editor.commit();
										dialog.dismiss();
										Intent eat = new Intent(Eat.this,
												Eat.class);
										startActivity(eat);
										finish();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();

					} 
					/*if filter by cuisine is chosen, display the further options
					 *works as above, but the sting is stored directly rather than its index*/
					  else if (item == 2) {
						final CharSequence[] items = { "Clear filter",
								"British", "Chinese", "French", "German",
								"Greek", "Indian", "Italian", "Japanese",
								"Lebanese","Mexican","Fast-food", "Other" };

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Eat.this);
						builder.setTitle("Choose a cuisine or clear filter");
						builder.setSingleChoiceItems(items, -1,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										editor.putString("cuisineFilter",
												(String) items[item]); 
										editor.commit();
										dialog.dismiss();
										Intent eat = new Intent(Eat.this,
												Eat.class);
										startActivity(eat);
										finish();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();
					}
                    /*if all filters have been cleared, reinitialize the variables and 
                     * re-launch the activity*/
					else {

						editor.putInt("distanceFilter", 0);

						editor.putInt("ratingFilter", 0);

						editor.putString("cuisineFilter", "Clear filter");
						editor.commit();

						Intent eat = new Intent(Eat.this, Eat.class);
						startActivity(eat);
						finish();

					}

				}
			});
			AlertDialog alert2 = builder.create();
			alert2.show();
			return true;
        
	    /*if restore deleted is selected,update reinitialize the variable storing deleted IDs */
		case R.id.restore:

			editor.putString("deletedId", "_");
			editor.commit();
			Toast.makeText(getBaseContext(), "Restored", Toast.LENGTH_SHORT)
					.show();
			Intent eat = new Intent(Eat.this, Eat.class);
			startActivity(eat);
			finish();
			return true;

		/*open the map*/
		case R.id.map:

			Bundle bundle = new Bundle();
			bundle.putInt("param", 1);
			Intent map = new Intent(this, Map.class);
			map.putExtras(bundle);
			startActivity(map);
			return true;

		}

		return false;
	}
}