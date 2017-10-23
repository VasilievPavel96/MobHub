package com.vasilievpavel.mobhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.adapter.RepositoryAdapter;
import com.vasilievpavel.mobhub.mvp.presenter.RepositoryPresenter;
import com.vasilievpavel.mobhub.mvp.view.LceView;
import com.vasilievpavel.mobhub.rest.model.Repository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepositoriesFragment extends MvpAppCompatFragment implements LceView<List<Repository>> {
    public static final String ARGUMENT_LOGIN = "ARGUMENT_LOGIN";
    public static final String TAG = "REPOS";
    @BindView(R.id.rv_list)
    RecyclerView list;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @InjectPresenter
    RepositoryPresenter presenter;
    RepositoryAdapter adapter;

    public static RepositoriesFragment newInstance(String login) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_LOGIN, login);
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String login = getArguments().getString(ARGUMENT_LOGIN);
        presenter.load(login);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupList();
        view.setTag(TAG);
    }

    private void setupList() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), manager.getOrientation());
        list.setLayoutManager(manager);
        adapter = new RepositoryAdapter(null);
        list.setAdapter(adapter);
        list.addItemDecoration(itemDecoration);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContent(List<Repository> data) {
        progressBar.setVisibility(View.GONE);
        adapter.changeData(data);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Fade fade = new Fade(Fade.IN);
            fade.setDuration(300);
            TransitionManager.beginDelayedTransition(list, fade);
        }
    }

    @Override
    public void showError(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(list, "Can't fetch repos", Snackbar.LENGTH_LONG).show();
    }

}
