package com.example.nailpolishapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.nailpolishapp.models.Polish;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class ShareTest {
    PolishShareMock share = new PolishShareMock();
    Polish polish =  new Polish("123", "John", "456", "https://example.com/image.jpg",
            "This is a comment", new ArrayList<String>(), new Date(), false);
    String receiver = "Alice";


    @Test
    public void testShareMessageValidInput() {


        // Test valid inputs
        boolean result = share.sharePolish(polish,receiver);
        assertTrue(result);


    }

    @Test
    public void testShareNullPolish(){
        // Test invalid Polish
        boolean result1 = share.sharePolish(null,receiver);
        assertFalse(result1);


    }
    @Test
    public void testShareEmptyReceiver(){
        //Test invalid reciver
        String receiverNone = "";
        boolean result2 = share.sharePolish(polish, receiverNone);
        assertFalse(result2);


    }


}
