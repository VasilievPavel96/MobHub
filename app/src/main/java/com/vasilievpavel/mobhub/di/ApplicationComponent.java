package com.vasilievpavel.mobhub.di;


import com.vasilievpavel.mobhub.adapter.RepositoryViewHolder;
import com.vasilievpavel.mobhub.mvp.presenter.FeedPresenter;
import com.vasilievpavel.mobhub.mvp.presenter.LoginPresenter;
import com.vasilievpavel.mobhub.mvp.presenter.ProfilePresenter;
import com.vasilievpavel.mobhub.mvp.presenter.RepositoryPresenter;
import com.vasilievpavel.mobhub.mvp.presenter.SearchPresenter;
import com.vasilievpavel.mobhub.ui.activity.LoginActivity;
import com.vasilievpavel.mobhub.ui.activity.MainActivity;
import com.vasilievpavel.mobhub.ui.fragment.ProfileFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ApplicationModule.class, StorageModule.class, RestModule.class})
@Singleton
public interface ApplicationComponent {
    void inject(LoginActivity activity);

    void inject(MainActivity activity);

    void inject(LoginPresenter presenter);

    void inject(FeedPresenter presenter);

    void inject(RepositoryPresenter presenter);

    void inject(ProfilePresenter presenter);

    void inject(SearchPresenter presenter);

    void inject(ProfileFragment fragment);

    void inject(RepositoryViewHolder holder);
}
