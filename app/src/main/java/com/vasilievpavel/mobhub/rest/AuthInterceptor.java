package com.vasilievpavel.mobhub.rest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    CurrentUser user;

    public AuthInterceptor(CurrentUser user) {
        this.user = user;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (user.getToken() != null) {
            Request req = chain.request().newBuilder()
                    .addHeader("Authorization", "token " + user.getToken())
                    .build();
            return chain.proceed(req);
        } else {
            return chain.proceed(chain.request());
        }
    }
}
