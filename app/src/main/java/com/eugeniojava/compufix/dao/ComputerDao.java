package com.eugeniojava.compufix.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.eugeniojava.compufix.model.Computer;

import java.util.List;

@Dao
public interface ComputerDao {

    @Query("SELECT * FROM computers")
    List<Computer> findAll();

    @Query("SELECT * FROM computers WHERE id = :id")
    Computer findById(long id);

    @Query("SELECT COUNT(*) FROM computers WHERE typeId = :typeId LIMIT 1")
    int countByTypeId(long typeId);

    @Insert
    long create(Computer computer);

    @Update
    void update(Computer computer);

    @Delete
    void delete(Computer computer);
}
