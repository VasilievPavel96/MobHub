package com.vasilievpavel.mobhub.mvp.view;


import com.arellomobile.mvp.MvpView;

public interface LceView<T> extends MvpView {
    void showLoading();

    void showContent(T data);

    void showError(Throwable throwable);
}
