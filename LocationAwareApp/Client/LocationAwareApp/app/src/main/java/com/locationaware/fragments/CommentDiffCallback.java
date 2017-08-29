package com.locationaware.fragments;

import android.support.v7.util.DiffUtil.Callback;

import com.locationaware.model.Comment;

import java.util.List;

/**
 * Created by dimasprawita on 29.08.17.
 */

public class CommentDiffCallback extends Callback {

    private final List<Comment> mOldCommentList;
    private final Comment mNewCommentList;

    public CommentDiffCallback(List<Comment> mOldCommentList, Comment mNewCommentList) {
        this.mOldCommentList = mOldCommentList;
        this.mNewCommentList = mNewCommentList;
    }


    @Override
    public int getOldListSize() {
        return mOldCommentList.size();
    }

    @Override
    public int getNewListSize() {
        return 1;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldCommentList.get(oldItemPosition).getCreatedat() == mNewCommentList.getCreatedat();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Comment oldComment = mOldCommentList.get(oldItemPosition);
        final Comment newComment = mNewCommentList;

        return oldComment.getComment().equals(newComment.getComment());
    }
}
