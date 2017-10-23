package com.vasilievpavel.mobhub.rest.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.vasilievpavel.mobhub.rest.model.Repository;

import java.util.List;

@Dao
public interface RepositoryDao {
    @Query("SELECT * FROM repository ORDER BY LOWER(name) ASC")
    List<Repository> getAll();

    @Query("SELECT * FROM repository WHERE LOWER(name) LIKE :name ORDER BY LOWER(name) ASC")
    List<Repository> findByName(String name);

    @Query("SELECT * FROM repository WHERE LOWER(name) LIKE :owner ORDER BY LOWER(name) ASC")
    List<Repository> findByOwner(String owner);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Repository> repos);
}
