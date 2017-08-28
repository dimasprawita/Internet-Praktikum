package com.locationaware.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.locationaware.R;
import com.locationaware.model.Comment;

import java.util.List;

/**
 * Created by dimasprawita on 27.08.17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    List<Comment> userCommentList;

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
        holder.deleteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userCommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView user_id, user_comment;
        private Button deleteComment;
        public ViewHolder(View view) {
            super(view);
            user_id= (TextView)view.findViewById(R.id.user_id);
            user_comment= (TextView)view.findViewById(R.id.user_comment);
            deleteComment = (Button) view.findViewById(R.id.delete_comment);
        }
    }

    private void doDeleteComment()
    {

    }

}
