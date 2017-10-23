package com.vasilievpavel.mobhub.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasilievpavel.mobhub.commons.OnItemClickListener;
import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.rest.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private List<User> users;
    private OnItemClickListener<User> listener;

    public UserAdapter(List<User> users, OnItemClickListener<User> listener) {
        this.users = users;
        this.listener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_user, viewGroup, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int i) {
        User user = users.get(i);
        userViewHolder.bindUser(user, listener);
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    public void changeData(List<User> newUsers) {
        users = newUsers;
        notifyDataSetChanged();
    }
}
