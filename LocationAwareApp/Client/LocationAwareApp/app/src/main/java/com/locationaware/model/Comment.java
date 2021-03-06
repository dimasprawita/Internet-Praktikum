package com.locationaware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model for comment. A comment has user ID,
 * comment typed by user, created time, and total like. For
 * each element of comment, there are setter and getter to
 * retrieve the value of the element. To deserialize the response,
 * we use gson annotations
 * Created by dimasprawita on 27.08.17.
 */

public class Comment {

    @SerializedName("userID")
    @Expose
    String userID;

    @SerializedName("comments")
    @Expose
    String comment;

    @SerializedName("created_at")
    @Expose
    String createdat;

    @SerializedName("total_like")
    @Expose
    int total_like = 0;

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

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public int getTotal_like() {
        return total_like;
    }

    public void setTotal_like(int total_like) {
        this.total_like = total_like;
    }
}
