package com.eugeniojava.compufix.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.eugeniojava.compufix.model.Computer;

import java.util.List;

@Dao
public interface ComputerDao {

    @Query("SELECT * FROM Computer")
    List<Computer> findAll();

    @Insert
    long create(Computer computer);
}
