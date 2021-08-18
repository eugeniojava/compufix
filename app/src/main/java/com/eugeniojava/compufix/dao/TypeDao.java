package com.eugeniojava.compufix.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.eugeniojava.compufix.model.Type;

import java.util.List;

@Dao
public interface TypeDao {

    @Query("SELECT * FROM types")
    List<Type> findAll();

    @Query("SELECT * FROM types WHERE id = :id")
    Type findById(long id);

    @Query("SELECT COUNT(*) FROM types")
    int countAll();

    @Query("SELECT COUNT(*) FROM types WHERE description = :description")
    int countByDescription(String description);

    @Insert
    long create(Type type);

    @Update
    void update(Type type);

    @Delete
    void delete(Type type);
}
