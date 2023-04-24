package com.example.nailpolishapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Polish implements Serializable{
    String userID, name, id, comment;
    String imageURL;
    ArrayList<String> type;
    Date createdAt;
    boolean liked;

    public Polish() {
    }

    public Polish(String userID, String name, String id, String imageURL,
                  String comment, ArrayList<String> type, Date createdAt, boolean liked) {
        this.userID = userID;
        this.name = name;
        this.id = id;
        this.imageURL = imageURL;
        this.comment = comment;
        this.type = type;
        this.createdAt = createdAt;
        this.liked = liked;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

}
