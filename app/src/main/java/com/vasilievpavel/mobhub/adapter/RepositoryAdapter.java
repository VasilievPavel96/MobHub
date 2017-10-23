package com.vasilievpavel.mobhub.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.rest.model.Repository;

import java.util.List;


public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryViewHolder> {
    private List<Repository> repos;

    public RepositoryAdapter(List<Repository> repos) {
        this.repos = repos;
    }

    @Override

    public RepositoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_repository, viewGroup, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder repositoryViewHolder, int i) {
        Repository repository = repos.get(i);
        repositoryViewHolder.bindRepository(repository);
    }

    @Override
    public int getItemCount() {
        return repos == null ? 0 : repos.size();
    }

    public void changeData(List<Repository> newRepos) {
        repos = newRepos;
        notifyDataSetChanged();
    }
}
