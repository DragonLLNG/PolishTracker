package com.example.nailpolishapp;

import com.example.nailpolishapp.models.Polish;

import java.util.ArrayList;
import java.util.List;

public class FindMock {

    public boolean FindPolish(String name, ArrayList<Polish> PolishArraylist){
        List<String> result = new ArrayList<>();
        for (int i = 1; i< PolishArraylist.size(); i++){
            if (PolishArraylist.get(i).getName().contains(name)) {
                return true;
            }
        }
       return false;
    }
}
