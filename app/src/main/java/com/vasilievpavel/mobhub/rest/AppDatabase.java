package com.vasilievpavel.mobhub.rest;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.vasilievpavel.mobhub.rest.dao.FeedDao;
import com.vasilievpavel.mobhub.rest.dao.RepositoryDao;
import com.vasilievpavel.mobhub.rest.dao.UserDao;
import com.vasilievpavel.mobhub.rest.model.FeedEntry;
import com.vasilievpavel.mobhub.rest.model.Repository;
import com.vasilievpavel.mobhub.rest.model.User;

@Database(entities = {User.class, Repository.class, FeedEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract RepositoryDao repoDao();

    public abstract FeedDao feedDao();
}
