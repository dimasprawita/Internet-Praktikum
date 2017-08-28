package com.locationaware.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dimasprawita on 27.08.17.
 */

public class PlaceComment {
    List<UserComment> userComments = new ArrayList<UserComment>();


    public List<UserComment> getUserComments() {
        return userComments;
    }

    public void setUserComments(List<UserComment> userComments) {
        this.userComments = userComments;
    }
}
