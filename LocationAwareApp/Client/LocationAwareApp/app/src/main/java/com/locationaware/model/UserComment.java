package com.locationaware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimasprawita on 27.08.17.
 */

public class UserComment {

    @SerializedName("venueID")
    @Expose
    String venueID;

    @SerializedName("comments")
    @Expose
    Comment comment;

    public String getVenueID() {
        return venueID;
    }

    public void setVenueID(String venueID) {
        this.venueID = venueID;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
