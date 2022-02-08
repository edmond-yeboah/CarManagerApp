package com.example.carmanagerapp;

import java.io.Serializable;

public class CarClass implements Serializable {
    private String make;
    private String model;
    private String transmission;
    private String chassis;
    private String color;
    private String owneremail;
    private String id;
    private String date;

    public CarClass() {
    }

    public CarClass(String make, String model, String transmission, String chassis, String color, String owneremail, String id,String date) {
        this.make = make;
        this.model = model;
        this.transmission = transmission;
        this.chassis = chassis;
        this.color = color;
        this.owneremail = owneremail;
        this.id = id;
        this.date = date;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOwneremail() {
        return owneremail;
    }

    public void setOwneremail(String owneremail) {
        this.owneremail = owneremail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate(){
        return date;
    }

    public void setDate(){
        this.date = date;
    }
}
