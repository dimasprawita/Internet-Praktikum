package com.locationaware.model;
import java.util.ArrayList;
import java.util.List;

/**
 * Model for comments in a specific place
 * one place can have one or more comments
 * each comment is stored in a list
 * Created by dimasprawita on 27.08.17.
 */

public class PlaceComment {
    List<UserComment> userComments = new ArrayList<UserComment>();

    /**
     * Method that gets the list of users' comments
     * @return the list of user comments
     */
    public List<UserComment> getUserComments() {
        return userComments;
    }

    /**
     * Method that set the list of users' comments
     * @param userComments user comments to set
     */
    public void setUserComments(List<UserComment> userComments) {
        this.userComments = userComments;
    }
}
