package com.locationaware.fragments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.locationaware.R;
import com.locationaware.model.Comment;
import com.locationaware.network.RetrofitInterface;
import com.locationaware.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dimasprawita on 27.08.17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    List<Comment> userCommentList = new ArrayList<>();
    CommentAdapter CA;

    private RecyclerView recyclerView = CommentFragment.recyclerView;

    private String userName;
    private String comment;
    private String vID = CommentFragment.venueID;
    private int currentLike =0;
    private int newLike = 0;

    public CommentAdapter(List<Comment> comment){
        userCommentList = comment;
    }


    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card_row, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        holder.user_id.setText(userCommentList.get(position).getUserID());
        holder.user_comment.setText(userCommentList.get(position).getComment());
        holder.created_at.setText(userCommentList.get(position).getCreatedat());
        holder.total_like.setText(String.valueOf(userCommentList.get(position).getTotal_like()));
        holder.text_Like.setText("people like your comment");
        holder.likeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = holder.user_id.getText().toString();
                comment = holder.user_comment.getText().toString();
                currentLike = Integer.parseInt(holder.total_like.getText().toString());
                doLikeComment();
                notifyItemChanged(position);

            }
        });
        holder.deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDeleteComment();
                notifyItemRemoved(position);
            }
        });


        //CA.notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return userCommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView user_id, user_comment, created_at, total_like, text_Like;
        private Button deleteComment, likeComment;
        public ViewHolder(View view) {
            super(view);
            user_id= (TextView)view.findViewById(R.id.user_id);
            user_comment= (TextView)view.findViewById(R.id.user_comment);
            created_at = (TextView) view.findViewById(R.id.comment_created_at);
            deleteComment = (Button) view.findViewById(R.id.delete_comment);
            likeComment = (Button) view.findViewById(R.id.Like_comment);
            total_like = (TextView) view.findViewById(R.id.number_like);
            text_Like = (TextView) view.findViewById(R.id.text_like);

            //currentLike = Integer.parseInt(total_like.getText().toString());
        }
    }

    private void doDeleteComment()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface svc = retrofit.create(RetrofitInterface.class);
        Call<Comment> call = svc.deleteComment(vID,userName,comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                Log.d("RESPONSE","Successfully delete comment");
                for(int i = 0; i < userCommentList.size();i++)
                {
                    if(userCommentList.get(i).getCreatedat().matches(response.body().getCreatedat()))
                    {
                        userCommentList.remove(i);
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void doLikeComment()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface svc = retrofit.create(RetrofitInterface.class);
        Call<Comment> call = svc.likeComment(vID,userName,comment);
        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {

                newLike = response.body().getTotal_like();
                for(int i = 0; i < userCommentList.size();i++)
                {
                    if(userCommentList.get(i).getCreatedat().matches(response.body().getCreatedat()))
                    {
                        userCommentList.get(i).setTotal_like(newLike);
                        notifyDataSetChanged();
                    }
                }


                //CA = new CommentAdapter(userCommentList);
                //RecyclerView.Adapter adapter = new CommentAdapter(userCommentList);

                //recyclerView.setAdapter(adapter);
                //currentLike = response.body().getTotal_like();
                //Log.d("Response","Total Like"+response.body().toString());
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.d("onFailure", t.toString());
                //showSnackBarMessage("Check in failed");
            }
        });
    }



    /*private void showSnackBarMessage(String message) {

        if (getView() != null) {

            Snackbar.make(getView(),message,Snackbar.LENGTH_SHORT).show();
        }

    }*/

}
