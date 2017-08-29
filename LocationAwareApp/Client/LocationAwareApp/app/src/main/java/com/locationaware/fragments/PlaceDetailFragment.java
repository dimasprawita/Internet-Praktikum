package com.locationaware.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.locationaware.R;
import com.locationaware.model.CheckIn;
import com.locationaware.model.ExamplePlaceDetail;
import com.locationaware.model.OpeningHours;
import com.locationaware.network.RetrofitInterface;
import com.locationaware.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dimasprawita on 07.08.17.
 */

public class PlaceDetailFragment extends Fragment {

    GoogleApiClient googleApiClient;
    private static final String Google_API_Key = "AIzaSyD4msknaxGUuC2ZIA5AVRulS9CCK62Dspo";
    public static final String TAG = PlaceDetailFragment.class.getSimpleName();

    private ImageView img;
    private String dId;
    private String mEmail;
    private String mName;
    private int mHeight, mWidth = 0;
    private int placeCheckIn = 0;
    private int userCheckIn = 0;

    private TextView detailName;
    private TextView detailAddr;
    private TextView latlang;
    private TextView phone;
    private TextView URI;
    private TextView openNow;
    private TextView priceLevel;
    private TextView checkinSum;
    private TextView checkinSumUser;
    
    private Button checkinBtn;
    private Button addComment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_detail,container,false);

        dId = getArguments().getString("Place_ID");
        mEmail = getArguments().getString(Constants.EMAIL);
        mName = getArguments().getString("Username");
        googleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();

        initViews(view);

        PhotoTask pt = new PhotoTask();
        pt.placePhotosAsync();

        loadPlaceDetail(dId);
        getCheckIn(dId,getArguments().getString(Constants.EMAIL));

        return view;
    }
    
    private void initViews(View v){
        detailName = (TextView) v.findViewById(R.id.displayName);
        detailAddr = (TextView) v.findViewById(R.id.displayAddr);
        latlang = (TextView) v.findViewById(R.id.displayLatLang);
        phone = (TextView) v.findViewById(R.id.displayPhone);
        URI= (TextView) v.findViewById(R.id.displayURI);
        openNow= (TextView) v.findViewById(R.id.displayOpenHour);
        priceLevel= (TextView) v.findViewById(R.id.displayPriceLevel);
        checkinSum = ((TextView) v.findViewById(R.id.checkin_sum));
        checkinSumUser = (TextView) v.findViewById(R.id.checkin_sum_user);
        checkinBtn = (Button) v.findViewById(R.id.checkin_btn);
        addComment = (Button) v.findViewById(R.id.comment_btn);

        checkinBtn.setOnClickListener(view -> createCheckIn(dId,getArguments().getString(Constants.EMAIL)));
        addComment.setOnClickListener(view -> goToComment());

        img = (ImageView) v.findViewById(R.id.imgView);
        img.post(() -> {
            mHeight = img.getHeight();
            mWidth = img.getWidth();
        });
    }


    class PhotoTask {

        private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
                = new ResultCallback<PlacePhotoResult>() {
            @Override
            public void onResult(PlacePhotoResult placePhotoResult) {
                if (!placePhotoResult.getStatus().isSuccess()) {
                    return;
                }

                img.setImageBitmap(placePhotoResult.getBitmap());
            }
        };

        private void placePhotosAsync() {
            final String placeId = dId;
            Places.GeoDataApi.getPlacePhotos(googleApiClient, placeId)
                    .setResultCallback(photos -> {
                        if (!photos.getStatus().isSuccess()) {
                            return;
                        }

                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        if (photoMetadataBuffer.getCount() > 0) {
                            // Display the first bitmap in an ImageView in the size of the view
                            photoMetadataBuffer.get(0)
                                    .getScaledPhoto(googleApiClient, mWidth, mHeight)
                                    .setResultCallback(mDisplayPhotoResultCallback);
                        }
                        photoMetadataBuffer.release();
                    });
        }


    }

    private void loadPlaceDetail(String placeID) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface svc = retrofit.create(RetrofitInterface.class);
        Call<ExamplePlaceDetail> call = svc.getPlaceDetail(placeID);
        call.enqueue(new Callback<ExamplePlaceDetail>() {
            @Override
            public void onResponse(Call<ExamplePlaceDetail> call, Response<ExamplePlaceDetail> response) {
                try{
                    Double lat = response.body().getResults().getGeometry().getLocation().getLat();
                    Double lng = response.body().getResults().getGeometry().getLocation().getLng();
                    String placeName = response.body().getResults().getName();
                    String vicinity = response.body().getResults().getVicinity();
                    OpeningHours openingHour = response.body().getResults().getOpeningHours();
                    String open="";
                    if(openingHour != null)
                    {
                        open = openingHour.getOpenNow().toString();
                    }

                    //String price = response.body().getResults().get(0).getPriceLevel().toString();
                    String phoneNumber = response.body().getResults().getPhoneNumber();
                    String web = response.body().getResults().getWebsite();

                    detailName.setText(placeName);
                    detailAddr.setText(vicinity);
                    latlang.setText("Lat: "+lat+" Lon: "+lng);
                    phone.setText(phoneNumber);
                    URI.setText(web);
                    if(open.matches("false"))
                    {
                        openNow.setText("It is closed");
                    }
                    else if(open.matches("true")) {
                        openNow.setText("It is open");
                    }
                    else{
                        openNow.setText("No information about opening hour");
                    }

                    //priceLevel.setText(price);
                }
                catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ExamplePlaceDetail> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void getCheckIn(String placeID, String userID){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface svc = retrofit.create(RetrofitInterface.class);
        Call<Integer> call = svc.getcheckIn(placeID);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                placeCheckIn = response.body();
                //placeCheckIn = Integer.parseInt(response.body().toString());
                if(placeCheckIn == 0){
                    checkinSum.setText("People never check in here");
                }
                else{
                    checkinSum.setText(placeCheckIn+" people have checked in here");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("onFailure", t.toString());
                showSnackBarMessage("Failed to show place's check ins");
            }
        });

        call = svc.getUserCheckIn(placeID,userID);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                userCheckIn = response.body();
                if(userCheckIn == 0) {
                    checkinSumUser.setText("You never check in here");
                }
                else if (userCheckIn == 1){
                    checkinSumUser.setText("You have checked in here "+userCheckIn+" time");
                }
                else {
                    checkinSumUser.setText("You have checked in here "+userCheckIn+" times");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("onFailure", t.toString());
                showSnackBarMessage("Failed to show user's check ins");
            }
        });
    }


    private void createCheckIn(String placeID, String userID){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface svc = retrofit.create(RetrofitInterface.class);
        Call<CheckIn> call = svc.postcheckIn(placeID, userID);
        call.enqueue(new Callback<CheckIn>() {
            @Override
            public void onResponse(Call<CheckIn> call, Response<CheckIn> response) {
                Log.d("RESPONSE",response.body().toString());
                placeCheckIn += 1;
                checkinSum.setText(placeCheckIn+" people have checked in here");
                userCheckIn +=1;
                if(userCheckIn == 1)
                {
                    checkinSumUser.setText("You have checked in here "+userCheckIn+" time");
                }
                else {
                    checkinSumUser.setText("You have checked in here "+userCheckIn+" times");
                }
                showSnackBarMessage("You have checked in");

            }

            @Override
            public void onFailure(Call<CheckIn> call, Throwable t) {
                Log.d("onFailure", t.toString());
                showSnackBarMessage("Check in failed");
            }
        });
    }

    private void goToComment()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Bundle extras = new Bundle();
        extras.putString("Place_ID", dId);
        extras.putString(Constants.EMAIL, mEmail);
        extras.putString("Username", mName);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(extras);
        ft.replace(R.id.fragmentPlaceDetail,fragment,CommentFragment.TAG);
        ft.commit();
    }

    private void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(),message,Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
