package com.eugeniojava.compufix.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "types", indices = @Index(value = {"description"}, unique = true))
public class Type {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String description;

    public Type(@NonNull String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
