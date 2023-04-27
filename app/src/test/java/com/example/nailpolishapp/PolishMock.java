package com.example.nailpolishapp;

import java.util.ArrayList;
import java.util.Date;

public class PolishMock {
    private final String name;
    private final ArrayList<String> type;
    private final Date createdAt;


    public PolishMock(String name, ArrayList<String> type, Date createdAt) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }

        for (String polishType : type) {
            if (!isValidType(polishType)) {
                throw new IllegalArgumentException("Invalid polish type: " + polishType);
            }
        }

        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
    }

    private boolean isValidType(String polishType) {
        return "gel".equals(polishType) || "regular".equals(polishType) || "powder".equals(polishType);
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}


