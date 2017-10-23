package com.vasilievpavel.mobhub.rest.model;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    private String accessToken;
    private String scope;
    @SerializedName("token_type")
    private String tokenType;


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
