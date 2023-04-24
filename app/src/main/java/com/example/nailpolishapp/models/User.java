package com.example.nailpolishapp.models;

public class User {
    String uid, name, profileImageURL;

    public User() {
    }

    public User(String uid, String name, String profileImageURL) {
        this.uid = uid;
        this.name = name;
        this.profileImageURL = profileImageURL;


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

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
