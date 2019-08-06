package com.hcl.InstantPickup.models;

import android.app.Application;

import com.google.gson.JsonObject;

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
    private JsonObject order_ready;

    public String getName() { return name; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZip() { return zip; }
    public JsonObject getReadyOrder() { return order_ready; }

    public void setName(String value) {
        this.name = value;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setReadyOrder(JsonObject order_ready) { this.order_ready = order_ready; }
}
