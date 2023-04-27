package com.example.firebase.model;

import android.widget.EditText;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;


@IgnoreExtraProperties
public class LostThing{

    public String city, description, name, email, imgPath;

    public LostThing() {
    }

    public LostThing(String city, String description, String name, String email, String imgPath) {
        this.city = city;
        this.description = description;
        this.name = name;
        this.email = email;
        this.imgPath = imgPath;
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
