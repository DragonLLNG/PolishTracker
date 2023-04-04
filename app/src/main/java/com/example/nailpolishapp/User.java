package com.example.nailpolishapp;

public class User {
    String uid, name;

    public User() {
    }

    public User(String uid, String name) {
        this.uid = uid;
        this.name = name;

    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
