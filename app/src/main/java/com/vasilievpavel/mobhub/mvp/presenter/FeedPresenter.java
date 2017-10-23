package com.vasilievpavel.mobhub.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vasilievpavel.mobhub.commons.CustomApplication;
import com.vasilievpavel.mobhub.mvp.view.LceView;
import com.vasilievpavel.mobhub.rest.GitHubApi;
import com.vasilievpavel.mobhub.rest.XmlParser;
import com.vasilievpavel.mobhub.rest.dao.FeedDao;
import com.vasilievpavel.mobhub.rest.model.FeedEntry;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

@InjectViewState
public class FeedPresenter extends MvpPresenter<LceView<List<FeedEntry>>> {
    @Inject
    GitHubApi gitHubApi;
    @Inject
    FeedDao feedDao;
    @Inject
    XmlParser parser;

    public FeedPresenter() {
        CustomApplication.getComponent().inject(this);
    }

    public void load(String username) {
        String author = username.toLowerCase();
        gitHubApi.getFeed(username)
                .map(ResponseBody::string)
                .map(parser::parseFeed)
                .flatMap(feed -> {
                    feedDao.insert(feed);
                    return Observable.just(feed);
                })
                .onErrorResumeNext(throwable -> {
                    return Observable.just(feedDao.findByAuthor(author));
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::showContent, getViewState()::showError,
                        () -> {}, disposable -> getViewState().showLoading());
    }
}
