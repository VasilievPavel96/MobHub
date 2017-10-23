package com.vasilievpavel.mobhub.di;


import android.content.SharedPreferences;

import com.vasilievpavel.mobhub.rest.AuthInterceptor;
import com.vasilievpavel.mobhub.rest.CurrentUser;
import com.vasilievpavel.mobhub.rest.GitHubApi;
import com.vasilievpavel.mobhub.rest.GitHubWebViewClient;
import com.vasilievpavel.mobhub.rest.XmlParser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RestModule {
    @Provides
    @Singleton
    public CurrentUser provideCurrentUser(SharedPreferences preferences) {
        return new CurrentUser(preferences);
    }

    @Provides
    public GitHubWebViewClient provideGitHubWebViewClient(GitHubApi gitHubApi) {
        return new GitHubWebViewClient(gitHubApi);
    }

    @Provides
    public OkHttpClient provideOkHttpClient(CurrentUser user) {
        AuthInterceptor interceptor = new AuthInterceptor(user);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    @Provides
    public Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(GitHubApi.BASE_URL)
                .build();
    }

    @Provides
    @Singleton
    public GitHubApi provideGitHubApi(Retrofit retrofit) {
        return retrofit.create(GitHubApi.class);
    }

    @Provides
    @Singleton
    public XmlParser provideXmlParser() {
        return new XmlParser();
    }
}
