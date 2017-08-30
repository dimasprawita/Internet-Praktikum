package com.locationaware.model;

/**
 * The Model for checkin. A check in has
 * place ID and user ID
 * Created by dimasprawita on 24.08.17.
 */

public class CheckIn {
    private String placeID;
    private String userID;

    /**
     * Method to get the place ID
     * @return {text} place ID
     */
    public String getPlaceID() {
        return placeID;
    }

    /**
     * MEthod to set the place ID
     * @param placeID placeID to set
     */
    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    /**
     * Method to get user ID
     * @return {text} user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Method to set user ID
     * @param userID userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

}
