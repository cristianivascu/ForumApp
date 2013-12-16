package com.example.forumapp;


/*class defining a place-object. A place can be either a restaurant, a bar/cafe or a hotel*/
public class Place {
	
	/*variables defining the description of a place*/
	private int mID;
	private String mPlaceName;
	private String mPlaceProperty;
	private String mPlaceAddress;
	private String mPlaceDistance;
	private int mPlaceRating;
	private String mPlacePhone;
	private String mPlaceWebsite;
	private String mPlaceType;
    
	/*the constructor; updates the description of a place*/
	public Place(int iD, String placeName, String placeProperty,
			String placeAddress, String placeDistance, int placeRating,
			String placePhone, String placeWebsite, String placeType) {
		this.mID = iD;
		this.mPlaceName = placeName;
		this.mPlaceProperty = placeProperty;
		this.mPlaceAddress = placeAddress;
		this.mPlaceDistance = placeDistance;
		this.mPlaceRating = placeRating;
		this.mPlacePhone = placePhone;
		this.mPlaceWebsite = placeWebsite;
		this.mPlaceType = placeType;
	}
    
	/*getter and setter methods*/
	public int getPlaceID() {
		return mID;
	}

	public void setPlaceID(int iD) {
		this.mID = iD;
	}

	public String getPlaceName() {
		return mPlaceName;
	}

	public void setPlaceName(String placeName) {
		this.mPlaceName = placeName;
	}

	public String getPlaceProperty() {
		return mPlaceProperty;
	}

	public void setPlaceProperty(String placeProperty) {
		this.mPlaceProperty = placeProperty;
	}

	public String getPlaceAddress() {
		return mPlaceAddress;
	}

	public void setPlaceAddress(String placeAddress) {
		this.mPlaceAddress = placeAddress;
	}

	public String getPlaceDistance() {
		return mPlaceDistance;
	}

	public void setPlaceDistance(String placeDistance) {
		this.mPlaceDistance = placeDistance;
	}

	public int getPlaceRating() {
		return mPlaceRating;
	}

	public void setPlaceRating(int placeRating) {
		this.mPlaceRating = placeRating;
	}

	public String getPlacePhone() {
		return mPlacePhone;
	}

	public void setPlacePhone(String placePhone) {
		this.mPlacePhone = placePhone;
	}

	public String getPlaceWebsite() {
		return mPlaceWebsite;
	}

	public void setPlaceWebsite(String placeWebsite) {
		this.mPlaceWebsite = placeWebsite;
	}

	public String getPlaceType() {
		return mPlaceType;
	}

	public void setPlaceType(String placeType) {
		this.mPlaceType = placeType;
	}
}