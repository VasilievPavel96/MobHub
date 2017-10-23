package com.vasilievpavel.mobhub.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.rest.model.FeedEntry;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedItemViewHolder> {
    private List<FeedEntry> feed;

    public FeedAdapter(List<FeedEntry> feed) {
        this.feed = feed;
    }

    @Override
    public FeedItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_feed, viewGroup, false);
        return new FeedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedItemViewHolder feedItemViewHolder, int i) {
        FeedEntry entry = feed.get(i);
        feedItemViewHolder.bindItem(entry);
    }

    @Override
    public int getItemCount() {
        return feed == null ? 0 : feed.size();
    }

    public void changeData(List<FeedEntry> newFeed) {
        feed = newFeed;
        notifyDataSetChanged();
    }
}
