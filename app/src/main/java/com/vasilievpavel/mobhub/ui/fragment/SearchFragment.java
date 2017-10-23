package com.vasilievpavel.mobhub.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.adapter.RepositoryAdapter;
import com.vasilievpavel.mobhub.adapter.UserAdapter;
import com.vasilievpavel.mobhub.commons.KeyboardUtils;
import com.vasilievpavel.mobhub.mvp.presenter.SearchPresenter;
import com.vasilievpavel.mobhub.mvp.view.SearchView;
import com.vasilievpavel.mobhub.rest.model.Repository;
import com.vasilievpavel.mobhub.rest.model.User;
import com.vasilievpavel.mobhub.ui.activity.MainActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SearchFragment extends MvpAppCompatFragment implements SearchView {
    public static final String TAG = "SEARCH";
    @BindView(R.id.rv_list)
    RecyclerView list;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    Toolbar mainToolbar;
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.search_toolbar)
    Toolbar searchToolbar;
    @BindView(R.id.search_options)
    ConstraintLayout searchOptions;
    @BindView(R.id.sortUsers)
    LinearLayout sortUsers;
    @BindView(R.id.sortRepos)
    LinearLayout sortRepos;
    @BindView(R.id.search_edit_text)
    EditText searchEditText;
    @InjectPresenter
    SearchPresenter presenter;
    RepositoryAdapter repositoryAdapter;
    UserAdapter userAdapter;
    boolean isOptionsMenuOpened = false;
    String sortBy = "Stars";
    String orderBy = "Asc";
    String searchFor = "Repos";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search_action) {
            showSearch();
        }
        return true;
    }

    public void hideSearch() {
        KeyboardUtils.hideSoftInput(getActivity());
        if (mainToolbar.getVisibility() != View.VISIBLE) {
            int cx = 0;
            int cy = 0;
            int endRadius = (int) Math.hypot(mainToolbar.getWidth(), mainToolbar.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(mainToolbar, cx, cy, 0, endRadius);
            anim.setDuration(500);
            mainToolbar.setVisibility(View.VISIBLE);
            searchToolbar.setVisibility(View.INVISIBLE);
            anim.start();
        }
        if (isOptionsMenuOpened) hideOptionsMenu();
    }

    private void showSearch() {
        int cx = searchToolbar.getRight();
        int cy = searchToolbar.getBottom();
        int endRadius = (int) Math.hypot(searchToolbar.getWidth(), searchToolbar.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(searchToolbar, cx, cy, 0, endRadius);
        anim.setDuration(500);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainToolbar.setVisibility(View.INVISIBLE);
            }
        });
        searchToolbar.setVisibility(View.VISIBLE);
        anim.start();
    }

    private void hideOptionsMenu() {
        float y = searchOptions.getY();
        searchOptions.animate().y(y + searchOptions.getHeight()).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                searchOptions.setVisibility(View.INVISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
                searchOptions.setY(y);
                isOptionsMenuOpened = !isOptionsMenuOpened;
            }
        }).start();
    }

    private void showOptionsMenu() {
        float y = searchOptions.getY();
        searchOptions.setY(y + searchOptions.getHeight());
        searchOptions.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.INVISIBLE);
        searchOptions.animate().y(y).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isOptionsMenuOpened = !isOptionsMenuOpened;
            }
        }).start();
    }

    @OnClick({R.id.btn_stars, R.id.btn_forks, R.id.btn_date, R.id.btn_asc,
            R.id.btn_desc, R.id.btn_repos, R.id.btn_users, R.id.btn_followers, R.id.btn_joined, R.id.btn_repo})
    public void optionChanged(View view) {
        LinearLayout linearLayout = (LinearLayout) view.getParent();
        int childCount = linearLayout.getChildCount();
        if (view.getId() == R.id.btn_repos) {
            sortBy = "Stars";
            sortUsers.setVisibility(View.INVISIBLE);
            sortRepos.setVisibility(View.VISIBLE);
        }
        if (view.getId() == R.id.btn_users) {
            sortBy = "Followers";
            sortUsers.setVisibility(View.VISIBLE);
            sortRepos.setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i < childCount; i++) {
            Button btn = (Button) linearLayout.getChildAt(i);
            if (btn.getId() == view.getId()) {
                if (i == 0) {
                    btn.setBackgroundResource(R.drawable.btn_border_left_fill);
                    btn.setTextColor(Color.WHITE);
                } else if (i == 1 && childCount == 2) {
                    btn.setBackgroundResource(R.drawable.btn_border_right_fill);
                    btn.setTextColor(Color.WHITE);
                } else if (i == 1 && childCount == 3) {
                    btn.setBackgroundResource(R.drawable.btn_border_fill);
                    btn.setTextColor(Color.WHITE);
                } else {
                    btn.setBackgroundResource(R.drawable.btn_border_right_fill);
                    btn.setTextColor(Color.WHITE);
                }
                if (linearLayout.getId() == R.id.sortRepos) {
                    sortBy = btn.getText().toString();
                } else if (linearLayout.getId() == R.id.sortUsers) {
                    sortBy = btn.getText().toString();
                } else if (linearLayout.getId() == R.id.orderBy) {
                    orderBy = btn.getText().toString();
                } else {
                    searchFor = btn.getText().toString();
                }
            } else {
                if (i == 0) {
                    btn.setBackgroundResource(R.drawable.btn_border_left);
                    btn.setTextColor(Color.BLACK);
                } else if (i == 1 && childCount == 2) {
                    btn.setBackgroundResource(R.drawable.btn_border_right);
                    btn.setTextColor(Color.BLACK);
                } else if (i == 1 && childCount == 3) {
                    btn.setBackgroundResource(R.drawable.btn_border);
                    btn.setTextColor(Color.BLACK);
                } else {
                    btn.setBackgroundResource(R.drawable.btn_border_right);
                    btn.setTextColor(Color.BLACK);
                }
            }
        }
    }

    @OnClick(R.id.options)
    public void onOptionsClick(View view) {
        if (!isOptionsMenuOpened) showOptionsMenu();
        else hideOptionsMenu();
    }

    @OnClick(R.id.arrow_back)
    public void onArrowClick(View view) {
        hideSearch();
    }

    @OnClick(R.id.btn_apply)
    public void optionsApplied(View view) {
        hideOptionsMenu();
        String q = searchEditText.getText().toString();
        presenter.search(q, sortBy, orderBy, searchFor);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mainToolbar = getActivity().findViewById(R.id.main_toolbar);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        setupList();
        displayRepos(null);
        view.setTag(TAG);
        RxTextView.textChanges(searchEditText)
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(charSeq -> !TextUtils.isEmpty(charSeq))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                .doOnNext(q -> presenter.search(q, sortBy, orderBy, searchFor))
                .subscribe();
    }

    private void setupList() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(manager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), manager.getOrientation());
        list.addItemDecoration(itemDecoration);
        repositoryAdapter = new RepositoryAdapter(null);
        MainActivity activity = (MainActivity) getActivity();
        userAdapter = new UserAdapter(null, user -> {
            hideSearch();
            activity.multiBackStack.addToBackStack(1, ProfileFragment.newInstance(user.getLogin()));
        });
        list.setAdapter(repositoryAdapter);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayUsers(List<User> data) {
        progressBar.setVisibility(View.GONE);
        userAdapter.changeData(data);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Fade fade = new Fade(Fade.IN);
            fade.setDuration(300);
            TransitionManager.beginDelayedTransition(list, fade);
        }
        list.setAdapter(userAdapter);
    }

    @Override
    public void displayRepos(List<Repository> data) {
        progressBar.setVisibility(View.GONE);
        repositoryAdapter.changeData(data);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Fade fade = new Fade(Fade.IN);
            fade.setDuration(300);
            TransitionManager.beginDelayedTransition(list, fade);
        }
        list.setAdapter(repositoryAdapter);
    }

    @Override
    public void showError(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_LONG).show();
    }

}
