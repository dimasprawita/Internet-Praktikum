package com.locationaware;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.locationaware.model.Response;
import com.locationaware.model.User;
import com.locationaware.network.NetworkUtil;
import com.locationaware.utils.Constants;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dimasprawita on 23.08.17.
 */

public class FindFriend extends AppCompatActivity {

    private Button search;
    private EditText friend;
    private RecyclerView recyclerView;
    private User friends;
    private DataAdapter adapter;

    private CompositeSubscription mSubscriptions;

    public static final String TAG = FindFriend.class.getSimpleName();

    private String mToken;
    private String mEmail;
    private String f_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_find_friend);
        mSubscriptions = new CompositeSubscription();
        getData();
        initViews();
    }

    private void getData() {
        Bundle extras = getIntent().getExtras();
        mToken = extras.getString(Constants.TOKEN);
        mEmail = extras.getString(Constants.EMAIL);
        Log.d("TOKEN",mToken);
    }

    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new DataAdapter(friends));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        search = (Button) findViewById(R.id.btn_search);
        friend = (EditText) findViewById(R.id.type_friend);
        search.setOnClickListener(view -> doFindFriend());
    }

    private void doFindFriend() {
        f_name = friend.getText().toString();
        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getFriends(mEmail,f_name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(User user) {
        friends = user;
        adapter = new DataAdapter(friends);
        recyclerView.setAdapter(adapter);
    }

    private void handleError(Throwable error) {

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                showSnackBarMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showSnackBarMessage("User Not Found");
        }
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.find_friend),message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
