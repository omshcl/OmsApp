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

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }
}
