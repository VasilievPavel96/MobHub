package com.vasilievpavel.mobhub.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.rest.model.FeedEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedItemViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_profile)
    ImageView profile;
    @BindView(R.id.tv_username)
    TextView username;
    @BindView(R.id.tv_date)
    TextView date;
    @BindView(R.id.tv_title)
    TextView title;

    public FeedItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindItem(FeedEntry entry) {
        Context context = profile.getContext();
        Glide.with(context).load(entry.getThumbnail()).apply(RequestOptions.circleCropTransform()).into(profile);
        username.setText(entry.getAuthor());
        date.setText(entry.getFormattedDate());
        title.setText(Html.fromHtml(entry.getTitle()));
    }
}
