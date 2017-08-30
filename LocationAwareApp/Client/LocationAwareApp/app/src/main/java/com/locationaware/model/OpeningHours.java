package com.locationaware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for OpeningHours object.
 * OpeningHours has openNow status that indicates if
 * a place is open or closed and weekday opening hours
 * Created by dimasprawita on 21.08.17.
 */

public class OpeningHours {
    @SerializedName("open_now")
    @Expose
    private Boolean openNow;
    @SerializedName("weekday_text")
    @Expose
    private List<Object> weekdayText = new ArrayList<Object>();

    /**
     * Method that indicates if place is open or closed
     * @return true if open and false if closed
     */
    public Boolean getOpenNow() {
        return openNow;
    }

    /**
     * Method that set the opennow status
     * @param openNow
     * The open_now
     */
    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }

    /**
     * Method that get the daily opening hours
     * @return
     * The weekdayText
     */
    public List<Object> getWeekdayText() {
        return weekdayText;
    }

    /**
     * Method that set daily opening hours
     * @param weekdayText
     * The weekday_text
     */
    public void setWeekdayText(List<Object> weekdayText) {
        this.weekdayText = weekdayText;
    }

}
