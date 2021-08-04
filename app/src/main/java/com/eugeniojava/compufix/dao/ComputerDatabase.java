package com.eugeniojava.compufix.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.eugeniojava.compufix.model.Computer;

@Database(entities = {Computer.class}, version = 1, exportSchema = false)
public abstract class ComputerDatabase extends RoomDatabase {

    private static ComputerDatabase instance;

    public static ComputerDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (ComputerDatabase.class) {
                if (instance == null) {
                    instance =
                            Room.databaseBuilder(context, ComputerDatabase.class, "computers.db")
                                    .allowMainThreadQueries()
                                    .build();
                }
            }
        }
        return instance;
    }

    public abstract ComputerDao computerDao();
}
