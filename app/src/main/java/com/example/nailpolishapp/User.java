package com.example.nailpolishapp;

public class User {
    public User(String name, String email, String password) {

    }
    String uid, name, email, password;

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
