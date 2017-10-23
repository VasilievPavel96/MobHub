package com.vasilievpavel.mobhub.di;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vasilievpavel.mobhub.rest.AppDatabase;
import com.vasilievpavel.mobhub.rest.dao.FeedDao;
import com.vasilievpavel.mobhub.rest.dao.RepositoryDao;
import com.vasilievpavel.mobhub.rest.dao.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class StorageModule {
    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    public AppDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "mobhub-database").build();
    }

    @Provides
    public UserDao provideUserDao(AppDatabase db) {
        return db.userDao();
    }

    @Provides
    public RepositoryDao provideRepositoryDao(AppDatabase db) {
        return db.repoDao();
    }

    @Provides
    public FeedDao provideFeedDao(AppDatabase db) {
        return db.feedDao();
    }

}
