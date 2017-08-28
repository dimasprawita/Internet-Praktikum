package com.locationaware.network;

import com.locationaware.model.CheckIn;
import com.locationaware.model.Comment;
import com.locationaware.model.Example;
import com.locationaware.model.ExamplePlaceDetail;
import com.locationaware.model.Response;
import com.locationaware.model.ResponseFriend;
import com.locationaware.model.ResponseImg;
import com.locationaware.model.User;
import com.locationaware.model.UserComment;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RetrofitInterface {

    @POST("users")
    Observable<Response> register(@Body User user);

    @POST("authenticate")
    Observable<Response> login();

    @GET("users/{email}")
    Observable<User> getProfile(@Path("email") String email);

    @PUT("users/{email}")
    Observable<Response> changePassword(@Path("email") String email, @Body User user);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordInit(@Path("email") String email);

    @POST("users/{email}/password")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);

    @DELETE("users/{email}")
    Observable<User> deleteUser(@Path("email") String userID);

    @GET("places/")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

    @GET("places/{placeID}")
    Call<ExamplePlaceDetail> getPlaceDetail(@Path("placeID") String placeID);

    @Multipart
    @POST("images/upload")
    Call<ResponseImg> uploadImage(@Part MultipartBody.Part image);

    @GET("users/{email}/search")
    //Observable<User> getFriends(@Path("email") String userID, @Query("name") String name);
    Observable<User> getFriends(@Path("email") String userID, @Query("name") String name);

    @POST("users/friends/add/{email}")
    Call<ResponseFriend> addFriend(@Path("email") String friends_id);

    @GET("places/{placeID}/checkin")
    Call<Integer> getcheckIn(@Path("placeID") String placeID);

    @GET("places/{placeID}/checkin")
    Call<Integer> getUserCheckIn(@Path("placeID") String placeID, @Query("email") String userID);

    @POST("places/{placeID}/checkin")
    Call<CheckIn> postcheckIn(@Path("placeID") String placeID, @Query("email") String userID);

    @GET("places/{placeID}/comments")
    Call<List<UserComment>> getPlaceComments(@Path("placeID") String placeID);

    @POST("places/{placeID}/comments")
    Call<Comment> createPlaceComments(@Path("placeID") String placeID, @Query("email") String userID, @Query("comment") String comment);



}
