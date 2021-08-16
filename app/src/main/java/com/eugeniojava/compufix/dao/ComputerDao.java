package com.eugeniojava.compufix.dao;

import androidx.room.Dao;
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

    @Insert
    long create(Computer computer);

    @Update
    void update(Computer computer);
}
