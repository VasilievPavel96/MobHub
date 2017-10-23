package com.vasilievpavel.mobhub.rest.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.vasilievpavel.mobhub.rest.model.FeedEntry;
import com.vasilievpavel.mobhub.rest.model.Repository;

import java.util.List;

@Dao
public interface FeedDao {
    @Query("SELECT * FROM feedentry")
    List<FeedEntry> getAll();

    @Query("SELECT * FROM feedentry WHERE LOWER(author)=:author")
    List<FeedEntry> findByAuthor(String author);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FeedEntry> entries);
}
