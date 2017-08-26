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

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private User friend;

    public DataAdapter(User friend) {
        this.friend = friend;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView friend_name,friend_mail;
        public ViewHolder(View view) {
            super(view);

            friend_name= (TextView)view.findViewById(R.id.friend_name);
            friend_mail= (TextView)view.findViewById(R.id.friend_mail);
        }
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        holder.friend_name.setText(friend.getName());
        holder.friend_mail.setText(friend.getEmail());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
