package com.locationaware;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.locationaware.fragments.PlaceDetailFragment;

/**
 * Created by dimasprawita on 27.08.17.
 */

public class PlaceDetailActivity extends AppCompatActivity{

    public static final String TAG = PlaceDetailActivity.class.getSimpleName();

    private PlaceDetailFragment placeDetailFragment;
    private String dId;
    private String mEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placedetail);

        if (savedInstanceState == null) {

            loadFragment();
        }
    }

    private void loadFragment(){
        if (placeDetailFragment == null) {

            placeDetailFragment = new PlaceDetailFragment();
            placeDetailFragment.setArguments(getIntent().getExtras());
        }
        getFragmentManager().beginTransaction().replace(R.id.fragmentPlaceDetail,placeDetailFragment,PlaceDetailFragment.TAG).commit();
    }
}
