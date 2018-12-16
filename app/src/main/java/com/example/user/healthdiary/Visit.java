package com.example.user.healthdiary;

import java.io.Serializable;

public class Visit implements Serializable {
    String id;
    String name;
    String email;
    String phone;
    String prescription;
    String date;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPrescription() {
        return prescription;
    }

    public String getDate() {
        return date;
    }

    public Visit(){

    }

    public Visit(String id, String name, String email, String phone, String prescription, String date) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.prescription = prescription;
        this.date = date;
    }
}
