package com.example.nailpolishapp;

import java.util.HashMap;
import java.util.Map;

public class Login {

    // Define a list of registered users (in real life, this data would be stored in a database)
    private static final Map<String, String> REGISTERED_USERS = new HashMap<>();

    static {
        // Add some sample registered users (in real life, this data would be generated dynamically or stored in a database)
        REGISTERED_USERS.put("testuser@example.com", "mypassword123");
        REGISTERED_USERS.put("anotheruser@example.com", "anotherpassword456");
    }

    /**
     * Attempt to log in a user with the provided email and password.
     *
     * @param email The email address of the user to log in.
     * @param password The password of the user to log in.
     * @return True if the login was successful, false otherwise.
     */
    public boolean login(String email, String password) {
        if (email == null || password == null) {
            return false; // Invalid input data
        }

        // Check if the email is registered
        if (!REGISTERED_USERS.containsKey(email)) {
            return false; // Email is not registered
        }

        // Check if the password is correct
        String expectedPassword = REGISTERED_USERS.get(email);
        return expectedPassword.equals(password);
    }
}
