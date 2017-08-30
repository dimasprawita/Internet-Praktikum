package com.locationaware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for list of friends that a user has.
 * Created by dimasprawita on 29.08.17.
 */

public class Friend {

    @SerializedName("friend")
    @Expose
    private List<Object> friend = new ArrayList<>();

    /**
     * Method to get list of friends
     * @return list of friends
     */
    public List<Object> getFriend() {
        return friend;
    }

    /**
     * MEthod to set list of friends
     * @param friend friend to set
     */
    public void setFriend(List<Object> friend) {
        this.friend = friend;
    }
}
