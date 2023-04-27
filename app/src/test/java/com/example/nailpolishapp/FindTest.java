package com.example.nailpolishapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.nailpolishapp.models.Polish;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class FindTest {


    Polish polish1 = new Polish("123", "Green OPI", "456", "https://example.com/image.jpg",
                                                       "This is a comment", new ArrayList<String>() , new Date(), false);
    Polish polish2 = new Polish("124", "Green Essie", "457", "https://example.com/image.jpg",
            "This is a comment 2", new ArrayList<String>() , new Date(), true);
    Polish polish3 = new Polish("125", "Blue", "458", "https://example.com/image.jpg",
            "This is a comment 3", new ArrayList<String>() , new Date(), false);

    ArrayList<Polish> polishArrayList = new ArrayList<>();

    FindMock findMock = new FindMock();


    @Test
    public void TestFindSuccess(){
        polishArrayList.add(polish1);
        polishArrayList.add(polish2);
        polishArrayList.add(polish3);

        boolean result = findMock.FindPolish("Green",polishArrayList);
        assertTrue(result);
    }

    @Test
    public void TestFindSucess2(){
        polishArrayList.add(polish1);
        polishArrayList.add(polish2);
        polishArrayList.add(polish3);

        boolean result1 = findMock.FindPolish("Blue",polishArrayList);
        assertTrue(result1);

    }

    @Test
    public void TestFindFail(){
        polishArrayList.add(polish1);
        polishArrayList.add(polish2);
        polishArrayList.add(polish3);
        boolean result2 = findMock.FindPolish("Red",polishArrayList);
        assertFalse(result2);

    }
}
