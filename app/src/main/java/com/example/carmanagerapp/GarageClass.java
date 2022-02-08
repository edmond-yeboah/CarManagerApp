package com.example.carmanagerapp;

import java.io.Serializable;

public class GarageClass implements Serializable {
    private String name;
    private String openhours;
    private String closehours;
    private String owneremail;
    private String addedon;
    private Double lat;
    private Double lon;
    private String country;
    private String city;
    private String address;
    private String id;

    public GarageClass() {
    }

    public GarageClass(String name, String openhours, String closehours, String owneremail, String addedon, Double lat, Double lon, String country, String city, String address, String id) {
        this.name = name;
        this.openhours = openhours;
        this.closehours = closehours;
        this.owneremail = owneremail;
        this.addedon = addedon;
        this.lat = lat;
        this.lon = lon;
        this.country = country;
        this.city = city;
        this.address = address;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenhours() {
        return openhours;
    }

    public void setOpenhours(String openhours) {
        this.openhours = openhours;
    }

    public String getClosehours() {
        return closehours;
    }

    public void setClosehours(String closehours) {
        this.closehours = closehours;
    }

    public String getOwneremail() {
        return owneremail;
    }

    public void setOwneremail(String owneremail) {
        this.owneremail = owneremail;
    }

    public String getAddedon() {
        return addedon;
    }

    public void setAddedon(String addedon) {
        this.addedon = addedon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
