package com.locationaware.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dimasprawita on 04.07.17.
 */

public class NearbyPlaces extends AsyncTask<Object, String, String> {

    String googlePlaceData;
    GoogleMap googleMap;
    String url;
    String type;

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlaces", "doInBackground entered");
            googleMap = (GoogleMap) params[0];
            url = (String) params[1];
            URLConnection urlConnection = new URLConnection();
            googlePlaceData = urlConnection.readUrl(url);
            Log.d("GetNearbyPlaces", "doInBackground exit");
        }
        catch (Exception e) {
            Log.d("GetNearbyPlaces",e.toString());
        }
        return googlePlaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        Parser dataParser = new Parser();
        nearbyPlacesList = dataParser.parse(s);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute", "Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            String reference = googlePlace.get("reference");
            String placeID = googlePlace.get("place_id");
            String photoRef = googlePlace.get("photo_ref");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);
            markerOptions.snippet(vicinity+ " : " +placeID);
            googleMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

}
