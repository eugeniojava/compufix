package com.eugeniojava.compufix.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.eugeniojava.compufix.R;
import com.eugeniojava.compufix.model.Computer;
import com.eugeniojava.compufix.model.Type;

import java.util.concurrent.Executors;

@Database(entities = {Computer.class, Type.class}, version = 2)
public abstract class ComputerDatabase extends RoomDatabase {

    private static ComputerDatabase instance;

    public static ComputerDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (ComputerDatabase.class) {
                if (instance == null) {
                    RoomDatabase.Builder builder =
                            Room.databaseBuilder(context, ComputerDatabase.class, "computers.db");

                    builder.addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(() -> persistInitialTypes(context));
                        }
                    });
                    instance = (ComputerDatabase) builder.fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }

    private static void persistInitialTypes(final Context context) {
        String[] descriptions = context.getResources().getStringArray(R.array.initial_types);

        for (String description : descriptions) {
            Type type = new Type(description);
            instance.typeDao().create(type);
        }
    }

    public abstract ComputerDao computerDao();

    public abstract TypeDao typeDao();
}
