package com.vasilievpavel.mobhub.mvp.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vasilievpavel.mobhub.commons.CustomApplication;
import com.vasilievpavel.mobhub.mvp.view.LceView;
import com.vasilievpavel.mobhub.rest.GitHubApi;
import com.vasilievpavel.mobhub.rest.dao.RepositoryDao;
import com.vasilievpavel.mobhub.rest.model.Repository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class RepositoryPresenter extends MvpPresenter<LceView<List<Repository>>> {
    @Inject
    GitHubApi gitHubApi;
    @Inject
    RepositoryDao repoDao;

    public RepositoryPresenter() {
        CustomApplication.getComponent().inject(this);
    }

    public void load(String username) {
        String query = username.toLowerCase() + "/%";
        gitHubApi.getRepos(username)
                .flatMap(repos -> {
                    repoDao.insert(repos);
                    return Observable.just(repos);
                })
                .onErrorResumeNext(throwable -> {
                    return Observable.just(repoDao.findByOwner(query));
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showContent, getViewState()::showError,
                        () -> {}, disposable -> getViewState().showLoading());
    }
}
