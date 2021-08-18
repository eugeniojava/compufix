package com.eugeniojava.compufix.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "computers", foreignKeys = @ForeignKey(entity = Type.class, parentColumns = "id", childColumns =
        "typeId"))
public class Computer {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String owner;

    @NonNull
    private String model;

    @NonNull
    private String manufacturer;

    @NonNull
    private String description;

    @ColumnInfo(index = true)
    private int typeId;

    @Ignore
    private Type type;

    @NonNull
    private String customerType;

    private boolean urgent;

    private Date completionForecast;

    public Computer(@NonNull String owner, @NonNull String model, @NonNull String manufacturer,
                    @NonNull String description, @NonNull String customerType) {
        this.owner = owner;
        this.model = model;
        this.manufacturer = manufacturer;
        this.description = description;
        this.customerType = customerType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getOwner() {
        return owner;
    }

    public void setOwner(@NonNull String owner) {
        this.owner = owner;
    }

    @NonNull
    public String getModel() {
        return model;
    }

    public void setModel(@NonNull String model) {
        this.model = model;
    }

    @NonNull
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(@NonNull String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @NonNull
    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(@NonNull String customerType) {
        this.customerType = customerType;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public Date getCompletionForecast() {
        return completionForecast;
    }

    public void setCompletionForecast(Date completionForecast) {
        this.completionForecast = completionForecast;
    }
}
