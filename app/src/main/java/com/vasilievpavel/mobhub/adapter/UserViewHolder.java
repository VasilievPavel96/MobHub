package com.vasilievpavel.mobhub.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vasilievpavel.mobhub.commons.OnItemClickListener;
import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.rest.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_profile)
    ImageView profile;
    @BindView(R.id.tv_username)
    TextView username;

    public UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindUser(User user, OnItemClickListener<User> listener) {
        profile.setOnClickListener(view -> {
            listener.onItemClick(user);
        });
        Context context = profile.getContext();
        Glide.with(context).load(user.getThumbnail()).apply(RequestOptions.circleCropTransform()).into(profile);
        username.setText(user.getLogin());
    }
}
