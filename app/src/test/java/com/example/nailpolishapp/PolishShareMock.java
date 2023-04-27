package com.example.nailpolishapp;

import com.example.nailpolishapp.models.Polish;

public class PolishShareMock {
    public boolean sharePolish(Polish polish, String recipient){
        if (polish==null || recipient.isEmpty()){
            return false;
        }
        return true;
    }
}
