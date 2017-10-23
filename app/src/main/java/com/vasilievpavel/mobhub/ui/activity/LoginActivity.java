package com.vasilievpavel.mobhub.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.webkit.WebView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.commons.CustomApplication;
import com.vasilievpavel.mobhub.mvp.presenter.LoginPresenter;
import com.vasilievpavel.mobhub.mvp.view.LoginView;
import com.vasilievpavel.mobhub.rest.GitHubWebViewClient;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vasilievpavel.mobhub.rest.GitHubApi.AUTH_URL;
import static com.vasilievpavel.mobhub.rest.GitHubApi.CLIENT_ID;

public class LoginActivity extends MvpAppCompatActivity implements LoginView {
    @BindView(R.id.webView)
    WebView webView;
    @InjectPresenter
    LoginPresenter presenter;
    @Inject
    GitHubWebViewClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CustomApplication.getComponent().inject(this);
        ButterKnife.bind(this);
        webView.setWebViewClient(client);
        client.setCallBack(presenter);
        presenter.checkAuth();
    }

    @Override
    public void signIn() {
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl(String.format(AUTH_URL, CLIENT_ID));
    }

    @Override
    public void loginSuccess() {
        webView.setVisibility(View.GONE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void loginFailure() {
        Snackbar.make(webView, "Unable to sign in", Snackbar.LENGTH_LONG).show();
    }
}
