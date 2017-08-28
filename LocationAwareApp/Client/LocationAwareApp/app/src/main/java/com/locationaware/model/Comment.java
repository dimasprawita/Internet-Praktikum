package com.locationaware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dimasprawita on 27.08.17.
 */

public class Comment {

    @SerializedName("userID")
    @Expose
    String userID;

    @SerializedName("comments")
    @Expose
    String comment;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
