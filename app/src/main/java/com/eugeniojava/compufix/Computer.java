package com.eugeniojava.compufix;

public class Computer {

    private String owner;
    private String model;
    private String manufacturer;
    private String description;
    private String type;
    private String customerType;
    private boolean urgent;

    public Computer(String owner, String model, String manufacturer, String description, String type,
                    String customerType, boolean urgent) {
        this.owner = owner;
        this.model = model;
        this.manufacturer = manufacturer;
        this.description = description;
        this.type = type;
        this.customerType = customerType;
        this.urgent = urgent;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }
}
