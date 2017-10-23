package com.vasilievpavel.mobhub.commons;


import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class MultiBackStack {
    private List<Stack<Fragment>> backStack = new ArrayList<>();
    private FragmentManager fragmentManager;
    private int currentPage;

    public MultiBackStack(FragmentManager fragmentManager, int backStackSize) {
        this.fragmentManager = fragmentManager;
        for (int i = 0; i < backStackSize; i++) {
            Stack<Fragment> stack = new Stack<>();
            backStack.add(stack);
        }
        currentPage = 0;
    }

    public void navigateTo(int page) {
        Fragment currentPageFragment;
        if (!backStack.get(currentPage).isEmpty()) {
            currentPageFragment = backStack.get(currentPage).peek();
        } else {
            currentPageFragment = getFragment(page);
            backStack.get(currentPage).push(currentPageFragment);
            fragmentManager.beginTransaction()
                    .add(getContainerId(), currentPageFragment)
                    .commit();
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.detach(currentPageFragment);
        Fragment selectedPageFragment;
        if (backStack.get(page).isEmpty()) {
            selectedPageFragment = getFragment(page);
            backStack.get(page).push(selectedPageFragment);
            transaction.add(getContainerId(), selectedPageFragment);
        } else {
            selectedPageFragment = backStack.get(page).peek();
            transaction.attach(selectedPageFragment);
        }
        transaction.commit();
        currentPage = page;
    }

    public void addToBackStack(int page, Fragment fragment) {
        Fragment currentFragment = backStack.get(page).peek();
        backStack.get(page).push(fragment);
        fragmentManager.beginTransaction()
                .detach(currentFragment)
                .add(getContainerId(), fragment)
                .commit();
    }

    public boolean onBackPressed() {
        if (backStack.get(currentPage).size() >= 2) {
            Stack<Fragment> stack = backStack.get(currentPage);
            Fragment topFragment = stack.pop();
            Fragment previousFragment = stack.peek();
            fragmentManager.beginTransaction()
                    .remove(topFragment)
                    .attach(previousFragment)
                    .commit();
            return true;
        } else {
            return false;
        }
    }

    public abstract Fragment getFragment(int page);

    @IdRes
    public abstract int getContainerId();
}