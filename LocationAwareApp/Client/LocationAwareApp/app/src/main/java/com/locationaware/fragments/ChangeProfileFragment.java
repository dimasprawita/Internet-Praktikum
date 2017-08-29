package com.locationaware.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.locationaware.ProfileActivity;
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

import static com.locationaware.utils.Validation.validateFields;

/**
 * Created by dimasprawita on 29.08.17.
 */

public class ChangeProfileFragment extends DialogFragment {

    public interface Listener {

        void onProfileChanged();
    }

    public static final String TAG = ChangeProfileFragment.class.getSimpleName();

    private EditText mEtNewName;
    private EditText mEtNewAge;
    private EditText mEtNewCity;
    private Button BtChangeProfile;
    private Button mBtCancel;
    private TextView mTvMessage;
    private TextInputLayout mTiNewName;
    private TextInputLayout mTiNewAge;
    private TextInputLayout mTiNewCity;
    private ProgressBar mProgressBar;

    private CompositeSubscription mSubscriptions;

    private String mToken;
    private String mEmail;

    private Listener pListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_profile,container,false);
        mSubscriptions = new CompositeSubscription();
        getData();
        initViews(view);
        return view;
    }

    private void getData() {

        Bundle bundle = getArguments();
        mToken = bundle.getString(Constants.TOKEN);
        mEmail = bundle.getString(Constants.EMAIL);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        pListener = (ProfileActivity)context;
    }

    private void initViews(View v) {

        mEtNewName = (EditText) v.findViewById(R.id.et_new_name);
        mEtNewAge = (EditText) v.findViewById(R.id.et_new_age);
        mEtNewCity = (EditText) v.findViewById(R.id.et_new_city);
        mTiNewName = (TextInputLayout) v.findViewById(R.id.ti_new_name);
        mTiNewAge= (TextInputLayout) v.findViewById(R.id.ti_new_age);
        mTiNewCity = (TextInputLayout) v.findViewById(R.id.ti_new_city);
        mTvMessage = (TextView) v.findViewById(R.id.tv_message_change_profile);
        BtChangeProfile = (Button) v.findViewById(R.id.btn_change_profile_acc);
        mBtCancel = (Button) v.findViewById(R.id.btn_cancel_change_profile);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressChangeProfile);

        BtChangeProfile.setOnClickListener(view -> changeProfile());
        mBtCancel.setOnClickListener(view -> dismiss());
    }

    private void changeProfile(){
        setError();

        String newName= mEtNewName.getText().toString();
        int newAge= Integer.parseInt(mEtNewAge.getText().toString());
        String newCity = mEtNewCity.getText().toString();
        int err = 0;

        if (!validateFields(newName)) {

            err++;
            mTiNewName.setError("Name should not be empty !");
        }

        if (!validateFields(String.valueOf(newAge))) {

            err++;
            mTiNewAge.setError("Age should not be empty !");
        }

        if (!validateFields(newCity)) {

            err++;
            mTiNewCity.setError("City should not be empty !");
        }

        if (err == 0) {
            User user = new User();
            user.setName(newName);
            user.setAge(newAge);
            user.setCity(newCity);
            changeProfileProgress(user);
            mProgressBar.setVisibility(View.VISIBLE);

        }
    }

    private void changeProfileProgress(User user)
    {
        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).changeProfile(mEmail,user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void setError() {

        mTiNewName.setError(null);
        mTiNewAge.setError(null);
        mTiNewCity.setError(null);
    }

    private void handleResponse(Response response) {

        //showMessage(response.getMessage());
        pListener.onProfileChanged();
        mProgressBar.setVisibility(View.GONE);
        dismiss();
    }

    private void handleError(Throwable error) {

        mProgressBar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                Response response = gson.fromJson(errorBody,Response.class);
                showMessage(response.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showMessage("Network Error !");
        }
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


