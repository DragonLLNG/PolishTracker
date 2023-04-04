package com.example.nailpolishapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LoginTest {

    // Define test data for valid and invalid email addresses
    private static final String VALID_EMAIL = "testuser@example.com";
    private static final String INVALID_EMAIL = "testuser.example.com";

    // Define test data for valid and invalid passwords
    private static final String VALID_PASSWORD = "mypassword123";
    private static final String INVALID_PASSWORD = "123";

    // Define the login service to test
    private Login login = new Login();

    @Test
    public void testValidLogin() {
        // Test a valid email and password combination
        assertTrue(login.login(VALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void testInvalidEmail() {
        // Test an invalid email format
        assertFalse(login.login(INVALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void testInvalidPassword() {
        // Test an invalid password
        assertFalse(login.login(VALID_EMAIL, INVALID_PASSWORD));
    }

    @Test
    public void testEmptyEmail() {
        // Test an empty email
        assertFalse(login.login("", VALID_PASSWORD));
    }

    @Test
    public void testEmptyPassword() {
        // Test an empty password
        assertFalse(login.login(VALID_EMAIL, ""));
    }

    @Test
    public void testNullEmail() {
        // Test a null email
        assertFalse(login.login(null, VALID_PASSWORD));
    }

    @Test
    public void testNullPassword() {
        // Test a null password
        assertFalse(login.login(VALID_EMAIL, null));
    }

}


