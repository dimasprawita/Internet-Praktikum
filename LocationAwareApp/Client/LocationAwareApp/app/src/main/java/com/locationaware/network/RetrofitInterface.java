package com.locationaware.network;

import com.locationaware.model.CheckIn;
import com.locationaware.model.Comment;
import com.locationaware.model.Example;
import com.locationaware.model.ExamplePlaceDetail;
import com.locationaware.model.Friend;
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

/**
 * This interface acts as a connection to backend part.
 * GET -> to get the data
 * POST -> create data
 * PUT -> update data
 * DELETE -> delete the data
 */
public interface RetrofitInterface {

    /**
     * Method to register the user. The user data
     * will be attached in the body of the request
     * @param user the user that makes request
     * @return message indicates the request succeeded or not
     */
    @POST("users")
    Observable<Response> register(@Body User user);

    /**
     * Method to login to the application
     * @return message indicates the request succeeded or not
     */
    @POST("authenticate")
    Observable<Response> login();

    /**
     * Method to get the user profile based on
     * the user email
     * @param email user identification
     * @return the user's data
     */
    @GET("users/{email}")
    Observable<User> getProfile(@Path("email") String email);


    /**
     * Method that change the user's password
     * @param email indicate user id
     * @param user attach User's other data in request body
     * @return message indicates the request succeeded or not
     */
    @PUT("users/{email}")
    Observable<Response> changePassword(@Path("email") String email, @Body User user);

    /**
     * Method that change the user's profile
     * @param email indicates user id
     * @param user attach user's data in request body
     * @return message indicates the request succeeded or not
     */
    @PUT("users/{email}/profile")
    Observable<Response> changeProfile(@Path("email") String email, @Body User user);

    /**
     * Method that initialize reset password
     * @param email indicates user id
     * @return message indicates the request succeeded or not
     */
    @POST("users/{email}/password")
    Observable<Response> resetPasswordInit(@Path("email") String email);

    /**
     * Method that indicates reset password is finished
     * @param email indicates user id
     * @param user attach user's data in request body
     * @return message indicates the request succeeded or not
     */
    @POST("users/{email}/password")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);

    /**
     * Method that does delete user.
     * @param userID user to delete
     * @return deleted user
     */
    @DELETE("users/{email}")
    Observable<User> deleteUser(@Path("email") String userID);

    /**
     * MEthod to get nearby places.
     * @param type query to search (bank, restaurant, or other places)
     * @param location user's current location
     * @param radius the radius of search
     * @return object example contains google place API json response
     */
    @GET("places/")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

    /**
     * Method that get the details of a place
     * @param placeID place ID to get detail
     * @return object example contains google place API json response
     */
    @GET("places/{placeID}")
    Call<ExamplePlaceDetail> getPlaceDetail(@Path("placeID") String placeID);

    /**
     * Method to upload an image
     * @param image the image file
     * @return message indicates the status of the request and response
     */
    @Multipart
    @POST("images/upload")
    Call<ResponseImg> uploadImage(@Part MultipartBody.Part image);

    /**
     * Method that search for users
     * @param userID user's email to search
     * @param name specific name of the user
     * @return
     */
    @GET("users/{email}/search")
    Observable<User> getFriends(@Path("email") String userID, @Query("name") String name);

    /**
     * Method that gets the user's friends
     * @param uID indicates user ID
     * @return list of user's friends
     */
    @GET("users/friends/{email}")
    Observable<Friend> getFriendList(@Path("email") String uID);

    /**
     * Method that add a user as a firend
     * @param friends_id other user's ID
     * @return
     */
    @POST("users/{requester}/{requested}")
    Call<Response> addFriend(@Path("requester") String uID, @Path("requested") String requested);

    /**
     * Method that get the number of place's check ins
     * @param placeID place ID
     * @return number of place's check ins
     */
    @GET("places/{placeID}/checkin")
    Call<Integer> getcheckIn(@Path("placeID") String placeID);

    /**
     * MEthod that gets the number of user's check ins in specific place
     * @param placeID the current place ID
     * @param userID the current user ID
     * @return
     */
    @GET("places/{placeID}/checkin")
    Call<Integer> getUserCheckIn(@Path("placeID") String placeID, @Query("email") String userID);

    /**
     * Method that allows user to create checkIn. It will update
     * the number of check in in check in schema
     * @param placeID current place ID
     * @param userID current user ID
     * @return object check in.
     */
    @POST("places/{placeID}/checkin")
    Call<CheckIn> postcheckIn(@Path("placeID") String placeID, @Query("email") String userID);

    /**
     * Method that gets the place's comments
     * @param placeID current place ID
     * @return list of pllace's comments
     */
    @GET("places/{placeID}/comments")
    Call<List<UserComment>> getPlaceComments(@Path("placeID") String placeID);

    /**
     * Method that allows user to create new comment.
     * It will update the Comment schema in the backend
     * @param placeID current place ID
     * @param user current user name
     * @param comment comment typed by user
     * @return object comment created by user
     */
    @POST("places/{placeID}/comments")
    Call<Comment> createPlaceComments(@Path("placeID") String placeID, @Query("user") String user, @Query("comment") String comment);

    /**
     * Method that updated the number of like for each comment
     * @param placeID current place ID
     * @param user current user ID
     * @param comment comment typed the user
     * @return object comment got liked by current user
     */
    @PUT("places/{placeID}/comments")
    Call<Comment> likeComment(@Path("placeID") String placeID, @Query("user") String user, @Query("comment") String comment);

    /**
     * MEthod that deletes the user's comment
     * @param placeID current place ID
     * @param user user name who writes the comment
     * @param comment comment written by user
     * @return
     */
    @DELETE("places/{placeID}/comments")
    Call<Comment> deleteComment(@Path("placeID") String placeID, @Query("user") String user, @Query("comment") String comment);

}
