package com.example.nailpolishapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.nailpolishapp.models.Polish;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class PolishTestWithCommentAndLike {

    @Test
    public void testPolish() {
        // Create a Polish object
        Polish p = new Polish("123", "John", "456", "https://example.com/image.jpg",
                "This is a comment", new ArrayList<String>(), new Date(), false);

        // Check that the object was created correctly
        assertEquals("123", p.getUserID());
        assertEquals("John", p.getName());
        assertEquals("456", p.getId());
        assertEquals("https://example.com/image.jpg", p.getImageURL());
        assertEquals("This is a comment", p.getComment());
        assertTrue(p.getType().isEmpty());
        assertNotNull(p.getCreatedAt());
        assertFalse(p.isLiked());

        // Modify the object
        p.setComment("New comment");
        p.setLiked(true);

        // Check that the modifications were successful
        assertEquals("New comment", p.getComment());
        assertTrue(p.isLiked());
    }


}