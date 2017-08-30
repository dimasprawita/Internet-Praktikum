package com.locationaware;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.locationaware.model.Response;
import com.locationaware.model.User;
import com.locationaware.network.NetworkUtil;
import com.locationaware.network.RetrofitInterface;
import com.locationaware.utils.Constants;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dimasprawita on 23.08.17.
 */

public class FindFriendActivity extends AppCompatActivity {

    private Button search;
    private Button friendRequest;
    private EditText friend;

    private RecyclerView recyclerView;
    private User friends;

    private CompositeSubscription mSubscriptions;

    public static final String TAG = FindFriendActivity.class.getSimpleName();

    private String mToken;
    private String mEmail;
    private String f_name;
    private String requested;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        mSubscriptions = new CompositeSubscription();
        getData();
        initViews();
    }

    private void getData() {
        Bundle extras = getIntent().getExtras();
        mToken = extras.getString(Constants.TOKEN);
        mEmail = extras.getString(Constants.EMAIL);
        //Log.d("TOKEN",mToken);
    }

    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        search = (Button) findViewById(R.id.btn_search);
        friend = (EditText) findViewById(R.id.type_friend);
        friendRequest = (Button) findViewById(R.id.btn_send_req);
        search.setOnClickListener(view -> doFindFriend());
        friendRequest.setOnClickListener(view -> doSendRequest());
    }

    /**
     * Method that creates a request to backend to
     * find a specific user. It uses user's email and
     * the name user want to search as parameter.
     */
    private void doFindFriend() {
        f_name = friend.getText().toString();
        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getFriends(mEmail,f_name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }


    /**
     * Method that sends a friend request to specific user
     * It uses user's token to ensure that the user is
     * logged in to the app.
     */
    private void doSendRequest() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {

            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .addHeader("x-access-token", mToken)
                    .method(original.method(),original.body());
            return  chain.proceed(builder.build());

        });

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(httpClient.build())
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface svc = retrofit.create(RetrofitInterface.class);
        Call<Response> call = svc.addFriend(mEmail,requested);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                showSnackBarMessage(response.message());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                showSnackBarMessage("Send request failed");
            }
        });

    }
    /**
     * Method that handles the response.
     * It will pass the user to the adapter and
     * the adapter will display user's information
     * @param user user to handle
     */
    private void handleResponse(User user) {
        requested = user.getEmail();
        friends = user;
        RecyclerView.Adapter adapter = new FindFriendAdapter(friends);
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
