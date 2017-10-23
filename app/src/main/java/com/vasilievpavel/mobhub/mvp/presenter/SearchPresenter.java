package com.vasilievpavel.mobhub.mvp.presenter;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vasilievpavel.mobhub.commons.CustomApplication;
import com.vasilievpavel.mobhub.mvp.view.SearchView;
import com.vasilievpavel.mobhub.rest.GitHubApi;
import com.vasilievpavel.mobhub.rest.dao.RepositoryDao;
import com.vasilievpavel.mobhub.rest.dao.UserDao;
import com.vasilievpavel.mobhub.rest.model.Repository;
import com.vasilievpavel.mobhub.rest.model.SearchResults;
import com.vasilievpavel.mobhub.rest.model.User;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchView> {
    @Inject
    GitHubApi gitHubApi;
    @Inject
    RepositoryDao repoDao;
    @Inject
    UserDao userDao;

    public SearchPresenter() {
        CustomApplication.getComponent().inject(this);
    }

    public void search(String q, String sortBy, String orderBy, String searchFor) {
        String query = "%" + q.toLowerCase() + "%";
        if (searchFor.equals("Repos")) {
            gitHubApi.findRepos(q, sortBy, orderBy)
                    .flatMap(results -> {
                        repoDao.insert(results.getItems());
                        return Observable.just(results);
                    })
                    .onErrorResumeNext(throwable -> {
                        SearchResults<Repository> results = new SearchResults<>();
                        results.setItems(repoDao.findByName(query));
                        return Observable.just(results);
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(res -> res.getItems())
                    .subscribe(getViewState()::displayRepos, getViewState()::showError,
                            () -> {}, disposable -> getViewState().showLoading());
        } else {
            gitHubApi.findUsers(q, sortBy, orderBy)
                    .flatMap(results -> {
                        userDao.insert(results.getItems());
                        return Observable.just(results);
                    })
                    .onErrorResumeNext(throwable -> {
                        SearchResults<User> results = new SearchResults<>();
                        results.setItems(userDao.findByLogin(query));
                        return Observable.just(results);
                    })
                    .map(results -> results.getItems())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::displayUsers, getViewState()::showError,
                            () -> {}, disposable -> getViewState().showLoading());
        }
    }
}
