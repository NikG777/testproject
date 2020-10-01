package com.nikita.testproject.model;

public class Car {
    private String concern;
    private String brand;
    private int power;
    private String transmission;
    private int mileage;

    public Car(String concern, String brand, int power, String transmission, int mileage) {
        this.concern = concern;
        this.brand = brand;
        this.power = power;
        this.transmission = transmission;
        this.mileage = mileage;
    }

    public String getConcern() {
        return concern;
    }

    public void setConcern(String concern) {
        this.concern = concern;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }
}
