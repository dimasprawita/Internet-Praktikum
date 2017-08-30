package com.locationaware;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.locationaware.fragments.ChangePasswordDialog;
import com.locationaware.fragments.ChangeProfileFragment;
import com.locationaware.fragments.DeleteUserFragment;
import com.locationaware.fragments.FragmentShowFriends;
import com.locationaware.model.Response;
import com.locationaware.model.ResponseImg;
import com.locationaware.model.User;
import com.locationaware.network.NetworkUtil;
import com.locationaware.network.RetrofitInterface;
import com.locationaware.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ProfileActivity extends AppCompatActivity implements ChangePasswordDialog.Listener, ChangeProfileFragment.Listener {

    public static final String TAG = ProfileActivity.class.getSimpleName();

    private static final int INTENT_REQUEST_CODE = 100;

    private TextView mTvName;
    private TextView mTvEmail;
    private TextView mTvDate;
    private TextView mTvAge;
    private TextView mTvCity;
    private Button mBtChangePassword;
    private Button mBtChangeProfile;
    private Button mBtLogout;
    private Button mBtPlaces;
    private Button mBtDelUser;
    private Button mBtChangePicture;
    private Button mBtShowPicture;
    private Button mBtFindFriend;
    private Button mBtShowFriend;
    private String mImageUrl = "";
    private Uri uri;

    private ProgressBar mProgressbar;

    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mEmail;
    private String mName;

    private CompositeSubscription mSubscriptions;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mSubscriptions = new CompositeSubscription();
        initViews();
        initSharedPreferences();
        loadProfile();
    }

    private void initViews() {

        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvEmail = (TextView) findViewById(R.id.tv_email);
        mTvDate = (TextView) findViewById(R.id.tv_date);
        mTvAge= (TextView) findViewById(R.id.tv_age);
        mTvCity= (TextView) findViewById(R.id.tv_city);
        mBtChangePassword = (Button) findViewById(R.id.btn_change_password);
        mBtChangeProfile = (Button) findViewById(R.id.btn_change_profile);
        mBtLogout = (Button) findViewById(R.id.btn_logout);
        mProgressbar = (ProgressBar) findViewById(R.id.progress);
        mBtPlaces = (Button) findViewById(R.id.btn_location);
        mBtDelUser = (Button) findViewById(R.id.btn_user_del);
        mBtChangePicture = (Button) findViewById(R.id.button_chg_pict);
        mBtShowPicture = (Button) findViewById(R.id.button_show_pict);
        mBtFindFriend = (Button) findViewById(R.id.button_find_friend);
        mBtShowFriend = (Button) findViewById(R.id.button_show_friends);

        mBtChangePassword.setOnClickListener(view -> showDialog());
        mBtChangeProfile.setOnClickListener(view -> goToChangeProfile());
        mBtLogout.setOnClickListener(view -> logout());
        mBtPlaces.setOnClickListener(view -> showMap());
        mBtDelUser.setOnClickListener(view -> deleteUser());
        mBtChangePicture.setOnClickListener(view ->  changePicture());
        mBtShowPicture.setOnClickListener(view -> showPicture());
        mBtFindFriend.setOnClickListener(view -> findFriend());
        mBtShowFriend.setOnClickListener(view -> goToShowFriend());
    }

    private void changePicture() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");

        try{
            startActivityForResult(intent, INTENT_REQUEST_CODE);

        } catch (ActivityNotFoundException e) {

            e.printStackTrace();
        }

    }

    /**
     * Method that directs user to find friend
     */
    private void findFriend(){
        Intent i = new Intent(this,FindFriendActivity.class);
        i.putExtra(Constants.TOKEN, mToken);
        i.putExtra(Constants.EMAIL,mEmail);
        startActivity(i);
    }

    private void showPicture() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mImageUrl));
        startActivity(intent);
    }


    /**
     * Method that get user's token and email and keep it so
     * it remains consistent
     */
    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = mSharedPreferences.getString(Constants.TOKEN,"");
        mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
        Log.d("TOKEN", mToken);
        Log.d("EMAIL", mEmail);
    }

    /**
     * Method that provides logout functionality.
     */
    private void logout() {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.TOKEN,"");
        editor.apply();
        finish();
    }

    /**
     * Method that directs user to change password
     */
    private void showDialog(){

        ChangePasswordDialog fragment = new ChangePasswordDialog();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL,mEmail);
        bundle.putString(Constants.TOKEN,mToken);
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), ChangePasswordDialog.TAG);
    }

    /**
     * Method that directs user to discover places nearby
     */
    private void showMap(){
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra(Constants.EMAIL, mEmail);
        i.putExtra("Username",mName);
        startActivity(i);
    }

    /**
     * Method that directs user to change profile
     */
    private void goToChangeProfile(){
        ChangeProfileFragment fragment = new ChangeProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL,mEmail);
        bundle.putString(Constants.TOKEN,mToken);
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), ChangeProfileFragment.TAG);

    }

    /**
     * Method that directs user to show friend
     */
    private void goToShowFriend(){
        FragmentShowFriends fragment = new FragmentShowFriends();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL,mEmail);
        bundle.putString(Constants.TOKEN,mToken);
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), FragmentShowFriends.TAG);
    }

    /**
     * Method that directs user to delete current user
     */
    private void deleteUser() {
        DeleteUserFragment fragment = new DeleteUserFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL, mEmail);
        bundle.putString(Constants.TOKEN,mToken);
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), DeleteUserFragment.TAG);
    }

    /**
     * Method tat creates request to load user profile.
     * It uses user's token and email to build the request
     */
    private void loadProfile() {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getProfile(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    /**
     * Method that handle the response
     * It will set all the attribute of the user
     * and display it in the screen
     * @param user
     */
    private void handleResponse(User user) {

        mProgressbar.setVisibility(View.GONE);
        mTvName.setText(user.getName());
        mTvEmail.setText(user.getEmail());
        mTvDate.setText(user.getCreated_at());
        mTvAge.setText(String.valueOf(user.getAge()));
        mTvCity.setText(user.getCity());
        mName = user.getName();
    }

    /**
     * Method that handle the error
     * @param error
     */
    private void handleError(Throwable error) {

        mProgressbar.setVisibility(View.GONE);

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

            showSnackBarMessage("Network Error !");
        }
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_profile),message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INTENT_REQUEST_CODE) {

            if(resultCode == RESULT_OK) {

                try {
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    uploadImage(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /*if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImg = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImg,filePath, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePath[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }*/
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    private void uploadImage(byte[] imageBytes) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        Call<ResponseImg> call = retrofitInterface.uploadImage(body);
        //mProgressbar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ResponseImg>() {
            @Override
            public void onResponse(Call<ResponseImg> call, retrofit2.Response<ResponseImg> response) {

                //mProgressbar.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    ResponseImg responseBody = response.body();
                    mBtShowPicture.setVisibility(View.VISIBLE);
                    mImageUrl = Constants.BASE_URL + responseBody.getPath();
                    Log.d("RESPONSE","response "+responseBody.getMessage());

                } else {

                    ResponseBody errorBody = response.errorBody();

                    Gson gs = new GsonBuilder().create();

                    try {
                        ResponseImg errorResponse = gs.fromJson(errorBody.string(), ResponseImg.class);
                        Log.d("ERROR RESPONSE","error response "+errorResponse.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseImg> call, Throwable t) {

                mProgressbar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });
    }


    /**
     * Method that show the message after
     * the user changes the password.
     */
    @Override
    public void onPasswordChanged() {

        showSnackBarMessage("Password Changed Successfully !");
    }

    /**
     * Method that load the user's profile after the profile changed
     */
    @Override
    public void onProfileChanged() {
        loadProfile();
    }
}

