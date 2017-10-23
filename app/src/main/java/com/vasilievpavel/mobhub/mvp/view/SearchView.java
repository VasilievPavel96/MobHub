package com.vasilievpavel.mobhub.mvp.view;


import com.arellomobile.mvp.MvpView;
import com.vasilievpavel.mobhub.rest.model.Repository;
import com.vasilievpavel.mobhub.rest.model.User;

import java.util.List;

public interface SearchView extends MvpView{
    void showLoading();

    void displayUsers(List<User> data);

    void displayRepos(List<Repository> data);

    void showError(Throwable throwable);
}
