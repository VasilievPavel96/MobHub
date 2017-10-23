package com.vasilievpavel.mobhub.ui.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.adapter.FeedAdapter;
import com.vasilievpavel.mobhub.mvp.presenter.FeedPresenter;
import com.vasilievpavel.mobhub.mvp.view.LceView;
import com.vasilievpavel.mobhub.rest.model.FeedEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FeedFragment extends MvpAppCompatFragment implements LceView<List<FeedEntry>> {
    public static final String ARGUMENT_LOGIN = "ARGUMENT_LOGIN";
    public static final String TAG = "FEED";
    @BindView(R.id.rv_list)
    RecyclerView list;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @InjectPresenter
    FeedPresenter presenter;
    FeedAdapter adapter;

    public static FeedFragment newInstance(String login) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_LOGIN, login);
        FeedFragment fragment = new FeedFragment();
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
        view.setTag(TAG);
        setupList();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContent(List<FeedEntry> data) {
        progressBar.setVisibility(View.GONE);
        adapter.changeData(data);
        animateList();
    }

    @Override
    public void showError(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(list, "Can't fetch feed", Snackbar.LENGTH_LONG).show();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void animateList() {
        Fade fade = new Fade(Fade.IN);
        fade.setDuration(300);
        TransitionManager.beginDelayedTransition(list, fade);
    }

    private void setupList() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), manager.getOrientation());
        adapter = new FeedAdapter(null);
        list.setLayoutManager(manager);
        list.addItemDecoration(itemDecoration);
        list.setAdapter(adapter);
    }

}
