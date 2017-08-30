package com.locationaware.fragments;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.locationaware.R;
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
 * Created by dimasprawita on 22.08.17.
 */

public class DeleteUserFragment extends DialogFragment {

    public interface Listener {

        void onDeletedUser();
    }

    public static final String TAG = DeleteUserFragment.class.getSimpleName();

    private Button mBtCancel;
    private Button mBtDelete;
    private TextView mTvMessage;
    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;

    private String mToken;
    private String mEmail;
    private Listener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_delete_user,container,false);
        mSubscriptions = new CompositeSubscription();
        getData();
        initViews(view);
        return view;
    }


    /*private void initSharedPreferences() {

        mSharedPreferences = ;
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
    }*/

    private void getData() {

        Bundle bundle = getArguments();
        mToken = bundle.getString(Constants.TOKEN);
        mEmail = bundle.getString(Constants.EMAIL);
    }

    private void initViews(View v){
        mBtCancel = (Button) v.findViewById(R.id.btn_cancel_del);
        mBtDelete = (Button) v.findViewById(R.id.btn_yes_del);
        mTvMessage = (TextView) v.findViewById(R.id.delete_tv);

        mBtCancel.setOnClickListener(view -> dismiss());
        mBtDelete.setOnClickListener(view -> deleteUser());
    }

    /**
     * Method that creates request to delete user
     * After delete the user, we set the user's email and token to
     * empty.
     */
    private void deleteUser() {
        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).deleteUser(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.TOKEN,"");
        editor.apply();
        //finish();
    }

    private void handleResponse(User user) {

        //mProgressBar.setVisibility(View.GONE);
        mListener.onDeletedUser();
        dismiss();
    }

    private void handleError(Throwable error) {

        //mProgressBar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                showMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } /*else {

            showMessage("Network Error !");
        }*/
    }

    private void showMessage(String message) {

        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText(message);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }


}
