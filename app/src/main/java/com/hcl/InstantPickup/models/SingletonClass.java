package com.hcl.InstantPickup.models;

import android.app.Application;

public class SingletonClass extends Application {

    private static SingletonClass instance;

    public static SingletonClass getInstance() {
        if (instance == null)
            instance = new SingletonClass();
        return instance;
    }

    private SingletonClass() {
    }

    private String name;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zip;

    public String getName() {
        return name;
    }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZip() { return zip; }

    public void setName(String value) {
        this.name = value;
    }
}
