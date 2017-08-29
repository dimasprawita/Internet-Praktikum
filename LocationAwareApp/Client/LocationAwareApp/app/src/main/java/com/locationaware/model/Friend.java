package com.locationaware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dimasprawita on 29.08.17.
 */

public class Friend {

    @SerializedName("friend")
    @Expose
    private List<Object> friend = new ArrayList<>();

    public List<Object> getFriend() {
        return friend;
    }

    public void setFriend(List<Object> friend) {
        this.friend = friend;
    }
}
