package com.locationaware.fragments;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.locationaware.R;
import com.locationaware.model.Friend;
import com.locationaware.model.Response;
import com.locationaware.network.NetworkUtil;
import com.locationaware.utils.Constants;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dimasprawita on 29.08.17.
 */

public class FragmentShowFriends extends DialogFragment {

    public static final String TAG = FragmentShowFriends.class.getSimpleName();
    private TextView showFriends;
    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;

    private String mToken;
    private String mEmail;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_friends,container,false);
        mSubscriptions = new CompositeSubscription();
        getData();
        initViews(view);
        loadFriends();
        return view;
    }

    private void initViews(View v){
        showFriends = (TextView) v.findViewById(R.id.tv_show_friend);
    }

    private void getData() {
        Bundle bundle = getArguments();
        mToken = bundle.getString(Constants.TOKEN);
        mEmail = bundle.getString(Constants.EMAIL);
    }

    private void loadFriends()
    {
        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getFriendList(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(Friend friend) {
        //showFriends.setText(friend.getFriend().get(0).toString());
        Log.d("Friends",friend.getFriend().toString());
    }

    private void handleError(Throwable error) {

        //mProgressBar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                Log.d("RESPONSE", response.getMessage());
                //showMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } /*else {

            showMessage("Network Error !");
        }*/
    }

    /*private void showMessage(String message) {

        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText(message);

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }


}
