package com.vasilievpavel.mobhub.commons;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.ui.fragment.FeedFragment;
import com.vasilievpavel.mobhub.ui.fragment.ProfileFragment;
import com.vasilievpavel.mobhub.ui.fragment.RepositoriesFragment;
import com.vasilievpavel.mobhub.ui.fragment.SearchFragment;

public class MultiBackStackImpl extends MultiBackStack {
    String login;

    public MultiBackStackImpl(FragmentManager fragmentManager, int backStackSize, String login) {
        super(fragmentManager, backStackSize);
        this.login = login;
    }

    @Override
    public Fragment getFragment(int page) {
        Fragment fragment = null;
        switch (page) {
            case 0:
                fragment = FeedFragment.newInstance(login);
                break;
            case 1:
                fragment = new SearchFragment();
                break;
            case 2:
                fragment = RepositoriesFragment.newInstance(login);
                break;
            case 3:
                fragment = ProfileFragment.newInstance(login);
                break;
        }
        return fragment;
    }

    @Override
    public int getContainerId() {
        return R.id.container;
    }
}
