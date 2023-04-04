package com.example.nailpolishapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class PolishMockTest {

    @Test
    public void testPolish() {
        // create a new Polish object
        String name = "Test Polish";
        ArrayList<String> type = new ArrayList<>();
        type.add("gel");
        type.add("regular");
        type.add("powder");
        Date createdAt = new Date();
        PolishMock polish = new PolishMock(name, type, createdAt);

        // test the name property
        assertEquals("Test Polish", polish.getName());

        // test the type property
        assertEquals(3, polish.getType().size());
        assertEquals("gel", polish.getType().get(0));
        assertEquals("regular", polish.getType().get(1));
        assertEquals("powder", polish.getType().get(2));

        // test the createdAt property
        assertEquals(createdAt, polish.getCreatedAt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPolishWithNameEmpty() {
        // create a new Polish object with an empty name
        ArrayList<String> type = new ArrayList<>();
        type.add("gel");
        type.add("regular");
        type.add("powder");
        Date createdAt = new Date();
        PolishMock polish = new PolishMock("", type, createdAt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPolishWithInvalidType() {
        // create a new Polish object with an invalid type
        String name = "Test Polish";
        ArrayList<String> type = new ArrayList<>();
        type.add("invalid type");
        Date createdAt = new Date();
        PolishMock polish = new PolishMock(name, type, createdAt);
    }
}



