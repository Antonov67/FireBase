package com.example.firebase.ui;

import android.widget.EditText;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;


@IgnoreExtraProperties
public class LostThing{

    public String city, description, name, email;

    public LostThing() {
    }

    public LostThing(String city, String description, String name, String email) {
        this.city = city;
        this.description = description;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "LostThing{" +
                "city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
