package com.locationaware.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for a place detail. Searching nearby place
 * will return different json response with searching for
 * place detail. New model is made here to make the json
 * parsing easier
 * Created by dimasprawita on 24.08.17.
 */

public class ExamplePlaceDetail {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = new ArrayList<Object>();
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    @SerializedName("result")
    @Expose
    private Result results = new Result();
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
    public Result getResults() {
        return results;
    }

    /**
     * Method to set the list of results of nearby search
     * @param results
     * The results
     */
    public void setResults(Result results) {
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
