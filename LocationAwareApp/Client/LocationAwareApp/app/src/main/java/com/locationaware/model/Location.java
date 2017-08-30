package com.locationaware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model for Location object. Location has latitude
 * longitude.
 * Created by dimasprawita on 21.08.17.
 */

public class Location {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    /**
     * Method that get the latitude
     * @return
     * The lat
     */
    public Double getLat() {
        return lat;
    }

    /**
     * Method that set the latitude
     * @param lat
     * The lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * Method that get the longitude
     * @return
     * The lng
     */
    public Double getLng() {
        return lng;
    }

    /**
     * Method that set the longitude
     * @param lng
     * The lng
     */
    public void setLng(Double lng) {
        this.lng = lng;
    }
}
