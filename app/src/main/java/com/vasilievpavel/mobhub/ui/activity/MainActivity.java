package com.vasilievpavel.mobhub.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jakewharton.rxbinding2.support.design.widget.RxBottomNavigationView;
import com.vasilievpavel.mobhub.commons.MultiBackStackImpl;
import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.commons.BottomNavigationBehavior;
import com.vasilievpavel.mobhub.commons.BottomNavigationViewHelper;
import com.vasilievpavel.mobhub.commons.CustomApplication;
import com.vasilievpavel.mobhub.rest.CurrentUser;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @Inject
    CurrentUser user;
    public MultiBackStackImpl multiBackStack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        CustomApplication.getComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(mainToolbar);
        multiBackStack = new MultiBackStackImpl(getSupportFragmentManager(), 4, user.getLogin());
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.feed);
        RxBottomNavigationView.itemSelections(bottomNavigationView)
                .map(MenuItem::getItemId)
                .map(this::findPositionById)
                .doOnNext(position -> multiBackStack.navigateTo(position))
                .subscribe();
    }

    private Integer findPositionById(int id) {
        switch (id) {
            case R.id.feed:
                return 0;
            case R.id.search:
                return 1;
            case R.id.repository:
                return 2;
            case R.id.profile:
                return 3;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if (!multiBackStack.onBackPressed()) {
            finish();
        }
    }
}
