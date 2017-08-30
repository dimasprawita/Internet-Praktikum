package com.locationaware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model for geometry object from Google
 * API json response. Geometry has object Location
 * Created by dimasprawita on 21.08.17.
 */

public class Geometry {
    @SerializedName("location")
    @Expose
    private Location location;

    /**
     * Method that gets the object location
     * @return
     * The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Method that set the location.
     * @param location
     * The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }
}
