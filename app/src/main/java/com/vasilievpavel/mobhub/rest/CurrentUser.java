package com.vasilievpavel.mobhub.rest;

import android.content.SharedPreferences;

public class CurrentUser {

    private SharedPreferences preferences;
    private String token;
    private String login;

    public CurrentUser(SharedPreferences preferences) {
        this.preferences = preferences;
        restoreState();
    }

    public String getToken() {
        return token;
    }

    public String getLogin() {
        return login;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void saveState() {
        preferences.edit()
                .putString("GITHUB_LOGIN", login)
                .putString("GITHUB_TOKEN", token)
                .apply();
    }

    private void restoreState() {
        token = preferences.getString("GITHUB_TOKEN", null);
        login = preferences.getString("GITHUB_LOGIN", null);
    }
}
