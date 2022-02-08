package com.example.carmanagerapp;

import java.io.Serializable;

public class UserClass implements Serializable {
    private String fname;
    private String lname;
    private String email;
    private String uid;

    public UserClass() {
    }

    public UserClass(String fname, String lname, String email, String uid) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
