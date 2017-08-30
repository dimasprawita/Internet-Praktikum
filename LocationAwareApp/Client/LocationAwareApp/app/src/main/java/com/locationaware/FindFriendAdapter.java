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

    @Override
    public FindFriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Update the field with the data received from the response
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(FindFriendAdapter.ViewHolder holder, int position) {
        holder.friend_name.setText(friend.getName());
        holder.friend_mail.setText(friend.getEmail());
        holder.friend_age.setText(String.valueOf(friend.getAge()));
        holder.friend_city.setText(friend.getCity());
        /*holder.friend_name.setText(countries.get(position));
        holder.friend_mail.setText(countries.get(position));*/
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * The class that defines the field used in the display.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView friend_name,friend_mail,friend_age,friend_city;
        public ViewHolder(View view) {
            super(view);
            friend_name= (TextView)view.findViewById(R.id.friend_name);
            friend_mail= (TextView)view.findViewById(R.id.friend_mail);
            friend_age = (TextView)view.findViewById(R.id.friend_age);
            friend_city = (TextView)view.findViewById(R.id.friend_city);
        }
    }
}
