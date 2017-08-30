package com.locationaware.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.locationaware.R;
import com.locationaware.model.Comment;
import com.locationaware.model.UserComment;
import com.locationaware.network.RetrofitInterface;
import com.locationaware.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dimasprawita on 27.08.17.
 */

public class CommentFragment extends Fragment {

    public static final String TAG = CommentFragment.class.getSimpleName();
    public static String venueID;
    private String mEmail;
    private String mName;

    private EditText commentform;
    private Button postComment;

    public static RecyclerView recyclerView;

    private String comment;

    private List<Comment> commentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment,container,false);
        getData();
        initViews(view);
        loadPlaceComment(venueID);
        return view;
    }

    private void initViews(View v){
        recyclerView = (RecyclerView) v.findViewById(R.id.comment_card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        commentform = (EditText) v.findViewById(R.id.write_comment);
        postComment = (Button) v.findViewById(R.id.post_comment);

        postComment.setOnClickListener(view -> postComment());


    }

    private void getData(){
        venueID = getArguments().getString("Place_ID");
        mEmail = getArguments().getString(Constants.EMAIL);
        mName = getArguments().getString("Username");
    }

    /** Method that load all comments in current place
     * IT will create a request to the backend by passing the place's ID
     * as parameter. The response will show all the comment from users in
     * current place
     * @param placeID place to show the comment
     */
    private void loadPlaceComment(String placeID){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface svc = retrofit.create(RetrofitInterface.class);
        Call<List<UserComment>> call = svc.getPlaceComments(placeID);
        call.enqueue(new Callback<List<UserComment>>() {
            @Override
            public void onResponse(Call<List<UserComment>> call, Response<List<UserComment>> response) {
                commentList.clear();
                for(int i = 0; i < response.body().size(); i++)
                {
                    commentList.add(i,response.body().get(i).getComment());
                }
                Collections.reverse(commentList);
                RecyclerView.Adapter adapter = new CommentAdapter(commentList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<UserComment>> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    /**
     * Method that allows user to create a comment.
     * It creates a request to backend using retrofit inteface
     * And after the user posts a comment, the comment will appear in the screen.
     */
    private void postComment(){
        comment = commentform.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Comment newComment = new Comment();
        newComment.setUserID(mName);
        newComment.setComment(comment);

        RetrofitInterface svc = retrofit.create(RetrofitInterface.class);
        Call<Comment> call = svc.createPlaceComments(venueID,mName,comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                showSnackBarMessage("You have successfully posted a comment");
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

        loadPlaceComment(venueID);
    }

    private void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(),message,Snackbar.LENGTH_SHORT).show();
        }

    }
}
