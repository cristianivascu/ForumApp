package com.example.forumapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

/*this class defines the view of each place-object, using standard interface components*/
@SuppressLint("ViewConstructor")
class CustomAdapterView extends LinearLayout {
    /*constructor*/
	@SuppressWarnings("deprecation")
	public CustomAdapterView(Context context, Place place) {
		
		super(context);
		String LOG_TAG = "CustomAdapterView";
		Log.i(LOG_TAG," activity");
		
		/*store the type of the place ('Eat' corresponds to restaurants and so on)*/
		String plType = place.getPlaceType();
		String property;
        /*update the property string according to the type of place; the string will 
         *be used later in the class*/
		if (plType.equals("Eat"))
			property = "Cuisine: ";
		else if (plType.equals("Drink"))
			property = "Type: ";
		else
			property = "STB rating & category: \n";
        /*update ID and description of the view according to the place*/
		setId(place.getPlaceID());
		setContentDescription(place.getPlaceName());
		/* container is a horizontal layer, with the part on the left being the image of
		 * the place, and the part on the right being a text description */
		setOrientation(LinearLayout.HORIZONTAL);
		setPadding(0, 6, 0, 6);

		/*set parameters*/
		LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		Params.setMargins(6, 35, 6, 0);

		/*create an image view, which will display the image corresponding to the place*/
		ImageView ivLogo = new ImageView(context);

		/*load the image; First check the id, which for the provided entries, is equal to 
		 * the position of the place in the list. Then check in what list is the place we 
		 * want to update (is it in the restaurants, the cafes or the hotels list?). 
		 * Depending on this, load the corresponding image. If the entry is added by the 
		 * user, load the corresponding stock image  */
		switch (place.getPlaceID()) {
		case 0:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.pinkolive));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.kilimanjaro));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.nicolson));
			break;
		case 1:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.khushis));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.beetlejuice));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.tenhill));
			break;
		case 2:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ciaoroma));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.beanscene));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.hotelduvin));
			break;
		case 3:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.suruchi));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.bagels));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ibis));
			break;
		case 4:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.petitparis));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.elephant));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.bankhotel));
			break;
		case 5:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.redbox));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.north));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.eurohostel));
			break;
		case 6:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.beirut));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.counting));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.missoni));
			break;
		case 7:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.royalmcgregor));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.southsider));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.kenneth));
			break;
		case 8:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.cityrestaurant));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.bobbys));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.smartcity));
			break;
		case 9:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.thetower));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.malones));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.quartermile));
			break;
		case 10:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.mariachi));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.starbucks));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.radisson));
			break;
		case 11:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.kfc));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.teviot));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.metro));
			break;
		default:
			if (plType.equals("Eat"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.fork));
			else if (plType.equals("Drink"))
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.cup));
			else
				ivLogo.setImageDrawable(context.getResources().getDrawable(
						R.drawable.bed));
			break;
		}
		/*add the image*/
		addView(ivLogo, Params);

		/*now create the text layer containing the description of the place*/
		Params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		LinearLayout PanelV = new LinearLayout(context);
		PanelV.setOrientation(LinearLayout.VERTICAL);
		PanelV.setGravity(Gravity.BOTTOM);
    
		/*display the name of the place*/
		TextView textName = new TextView(context);
		textName.setTextSize(16);
		textName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		textName.setText(place.getPlaceName());
		PanelV.addView(textName);

		/*display the rating bar and update the rating*/
		LinearLayout.LayoutParams Params2 = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		RatingBar placeRating = new RatingBar(context, null,
				android.R.attr.ratingBarStyleSmall);
		placeRating.setLayoutParams(Params2);
		placeRating.setNumStars(5);
		placeRating.setStepSize(1);
		placeRating.setRating(place.getPlaceRating());
		PanelV.addView(placeRating);

		/*display the distance from the Forum*/
		TextView placeDistance = new TextView(context);
		placeDistance.setTextSize(16);
		placeDistance
				.setText("Distance: " + place.getPlaceDistance() + " min.");
		PanelV.addView(placeDistance);

		/*display the property defined at the beginning of the class*/
		TextView placeProperty = new TextView(context);
		placeProperty.setTextSize(16);
		placeProperty.setText(property + place.getPlaceProperty());
		PanelV.addView(placeProperty);

		/*display the address*/
		TextView placeAddress = new TextView(context);
		placeAddress.setTextSize(16);
		placeAddress.setText("Address: " + place.getPlaceAddress());
		PanelV.addView(placeAddress);

		/*display the phone number*/ 
		TextView placePhone = new TextView(context);
		placePhone.setTextSize(16);
		placePhone.setText("Tel: " + place.getPlacePhone());
		PanelV.addView(placePhone);

		/*display the phone number*/
		TextView placeWebsite = new TextView(context);
		placeWebsite.setTextSize(16);
		placeWebsite.setText(place.getPlaceWebsite());
		PanelV.addView(placeWebsite);

		/*update the description*/
		addView(PanelV, Params);

	}

}
