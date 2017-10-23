package com.vasilievpavel.mobhub.rest;

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vasilievpavel.mobhub.rest.model.Token;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;

import static com.vasilievpavel.mobhub.rest.GitHubApi.CLIENT_ID;
import static com.vasilievpavel.mobhub.rest.GitHubApi.CLIENT_SECRET;
import static com.vasilievpavel.mobhub.rest.GitHubApi.REDIRECT_URL;


public class GitHubWebViewClient extends WebViewClient {
    private Callback<Token> callBack;
    private GitHubApi gitHubApi;

    public GitHubWebViewClient(GitHubApi gitHubApi) {
        this.gitHubApi = gitHubApi;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Uri uri = Uri.parse(url);
        if (uri.getHost().equals(REDIRECT_URL.getHost())) {
            String code = uri.getQueryParameter("code");
            Call<Token> call = gitHubApi.getToken(CLIENT_ID, CLIENT_SECRET, code);
            call.enqueue(callBack);
            return true;
        } else {
            return false;
        }
    }

    public void setCallBack(Callback<Token> callBack) {
        this.callBack = callBack;
    }
}
