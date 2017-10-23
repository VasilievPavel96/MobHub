package com.vasilievpavel.mobhub.rest.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.vasilievpavel.mobhub.rest.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user WHERE login=:login LIMIT 1")
    User findUser(String login);

    @Query("SELECT * FROM user WHERE LOWER(login) LIKE :login ORDER BY LOWER(login) ASC")
    List<User> findByLogin(String login);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<User> users);
}
