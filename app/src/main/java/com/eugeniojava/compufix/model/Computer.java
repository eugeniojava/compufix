package com.eugeniojava.compufix.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Computer {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private String owner;

    @NonNull
    private String model;

    @NonNull
    private String manufacturer;

    @NonNull
    private String description;

    @NonNull
    private String type;

    @NonNull
    private String customerType;

    private boolean urgent;

    public Computer(@NonNull String owner, @NonNull String model, @NonNull String manufacturer,
                    @NonNull String description, @NonNull String type, @NonNull String customerType, boolean urgent) {
        this.owner = owner;
        this.model = model;
        this.manufacturer = manufacturer;
        this.description = description;
        this.type = type;
        this.customerType = customerType;
        this.urgent = urgent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
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
}
