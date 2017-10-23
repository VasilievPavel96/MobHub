package com.vasilievpavel.mobhub.mvp.view;


import com.arellomobile.mvp.MvpView;

public interface LoginView extends MvpView {
    void signIn();

    void loginSuccess();

    void loginFailure();
}
