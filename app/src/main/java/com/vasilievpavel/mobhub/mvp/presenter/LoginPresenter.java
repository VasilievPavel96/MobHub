package com.vasilievpavel.mobhub.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vasilievpavel.mobhub.commons.CustomApplication;
import com.vasilievpavel.mobhub.mvp.view.LoginView;
import com.vasilievpavel.mobhub.rest.CurrentUser;
import com.vasilievpavel.mobhub.rest.GitHubApi;
import com.vasilievpavel.mobhub.rest.model.Token;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> implements Callback<Token> {
    @Inject
    CurrentUser user;
    @Inject
    GitHubApi gitHubApi;

    public LoginPresenter() {
        CustomApplication.getComponent().inject(this);
    }

    public void checkAuth() {
        if (user.getToken() == null) {
            getViewState().signIn();
        } else if (user.getLogin() == null) {
            loadUserInfo();
        } else {
            getViewState().loginSuccess();
        }
    }

    @Override
    public void onResponse(Call<Token> call, Response<Token> response) {
        user.setToken(response.body().getAccessToken());
        user.saveState();
        loadUserInfo();
    }


    @Override
    public void onFailure(Call<Token> call, Throwable t) {
        getViewState().loginFailure();
    }

    private void loadUserInfo() {
        gitHubApi.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(userInfo -> {
                    String login = userInfo.getLogin();
                    user.setLogin(login);
                    user.saveState();
                    getViewState().loginSuccess();
                })
                .doOnError(throwable -> getViewState().loginFailure())
                .subscribe();
    }
}
