package com.locationaware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for Google API json response
 * GSON is used to deserialize the JSON response.
 * Each element on a response is provided with
 * getter and setter method.
 * Created by dimasprawita on 21.08.17.
 */

public class Example {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = new ArrayList<Object>();
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * Method to get the Html attributions object
     * @return The htmlAttributions
     */
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    /**
     * MEthod to set the Html Attributions
     * @param htmlAttributions
     * The html_attributions
     */
    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    /**
     * Method to get next page token value
     * @return
     * The nextPageToken
     */
    public String getNextPageToken() {
        return nextPageToken;
    }

    /**
     * Method to set next page token value
     * @param nextPageToken
     * The next_page_token
     */
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    /**
     * MEthod to get the list of results of nearby search
     * @return
     * The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * Method to set the list of results of nearby search
     * @param results
     * The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     * Method to get the status from json response
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Method to set the status of json response
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
