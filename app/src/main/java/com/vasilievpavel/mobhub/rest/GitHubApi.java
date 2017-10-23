package com.vasilievpavel.mobhub.rest;

import android.net.Uri;

import com.vasilievpavel.mobhub.rest.model.Repository;
import com.vasilievpavel.mobhub.rest.model.SearchResults;
import com.vasilievpavel.mobhub.rest.model.Token;
import com.vasilievpavel.mobhub.rest.model.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubApi {
    String CLIENT_ID = "f9ac50ab6dd49cb0c98a";
    String CLIENT_SECRET = "6acbb4040ea2dd8ab95468cc07858ca7994348aa";
    String AUTH_URL = "http://github.com/login/oauth/authorize?client_id=%s";
    String BASE_URL = "https://api.github.com/";
    Uri REDIRECT_URL = Uri.parse("http://example.com");

    @Headers("accept: application/json")
    @FormUrlEncoded
    @POST("https://github.com/login/oauth/access_token")
    Call<Token> getToken(@Field("client_id") String clientId,
                         @Field("client_secret") String clientSecret,
                         @Field("code") String code);

    @GET("https://github.com/{username}.atom")
    Observable<ResponseBody> getFeed(@Path("username") String username);

    @GET("users/{username}/repos")
    Observable<List<Repository>> getRepos(@Path("username") String username);

    @GET("user/repos")
    Observable<List<Repository>> getRepos();

    @GET("users/{username}")
    Observable<User> getUser(@Path("username") String username);

    @GET("user")
    Observable<User> getUser();

    @GET("search/repositories")
    Observable<SearchResults<Repository>> findRepos(@Query("q") String q, @Query("sort") String sort,
                                                    @Query("order") String order);

    @GET("/search/users")
    Observable<SearchResults<User>> findUsers(@Query("q") String q, @Query("sort") String sort,
                                              @Query("order") String order);

    @GET("https://github.com/users/{username}/contributions")
    Observable<ResponseBody> getContribution(@Path("username") String username);
}
