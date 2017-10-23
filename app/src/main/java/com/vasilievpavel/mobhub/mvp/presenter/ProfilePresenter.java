package com.vasilievpavel.mobhub.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vasilievpavel.mobhub.commons.CustomApplication;
import com.vasilievpavel.mobhub.mvp.view.LceView;
import com.vasilievpavel.mobhub.rest.GitHubApi;
import com.vasilievpavel.mobhub.rest.XmlParser;
import com.vasilievpavel.mobhub.rest.dao.UserDao;
import com.vasilievpavel.mobhub.rest.model.ProfileInfo;
import com.vasilievpavel.mobhub.rest.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

@InjectViewState
public class ProfilePresenter extends MvpPresenter<LceView<ProfileInfo>> {
    @Inject
    GitHubApi gitHubApi;
    @Inject
    UserDao userDao;
    @Inject
    XmlParser parser;

    public ProfilePresenter() {
        CustomApplication.getComponent().inject(this);
    }

    public void load(String username) {
        Observable<User> userObservable = gitHubApi.getUser(username.toLowerCase())
                .map(user -> {
                    userDao.insert(user);
                    return user;
                })
                .onErrorResumeNext(throwable -> {
                    return Observable.just(userDao.findUser(username));
                });
        Observable<List<Map<String, String>>> contributionsObservable = gitHubApi.getContribution(username)
                .map(ResponseBody::string)
                .map(parser::parseContributions)
                .onErrorResumeNext(throwable -> {
                    List<Map<String, String>> contrib = new ArrayList<>();
                    return Observable.just(contrib);
                });
        Observable.zip(userObservable, contributionsObservable, (user, contrib) -> new ProfileInfo(user, contrib))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showContent, getViewState()::showError,
                        () -> {
                        }, disposable -> getViewState().showLoading());
    }
}
