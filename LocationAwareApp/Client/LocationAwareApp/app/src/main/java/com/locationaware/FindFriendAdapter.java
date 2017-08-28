package com.locationaware;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.locationaware.model.User;

/**
 * Created by dimasprawita on 26.08.17.
 */

public class FindFriendAdapter extends RecyclerView.Adapter<FindFriendAdapter.ViewHolder> {

    private User friend;

    public FindFriendAdapter(User friend) {
        this.friend = friend;
    }


    /*private ArrayList<String> countries;

    public FindFriendAdapter(ArrayList<String> countries) {
        this.countries = countries;
    }*/



    @Override
    public FindFriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FindFriendAdapter.ViewHolder holder, int position) {
        holder.friend_name.setText(friend.getName());
        holder.friend_mail.setText(friend.getEmail());

        /*holder.friend_name.setText(countries.get(position));
        holder.friend_mail.setText(countries.get(position));*/
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView friend_name,friend_mail;
        public ViewHolder(View view) {
            super(view);
            friend_name= (TextView)view.findViewById(R.id.friend_name);
            friend_mail= (TextView)view.findViewById(R.id.friend_mail);
        }
    }
}
