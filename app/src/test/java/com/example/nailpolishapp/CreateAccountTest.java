package com.example.nailpolishapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CreateAccountTest {

    @Test
    public void testCreateAccount() {
        CreateAccount account = new CreateAccount();

        // Test valid inputs
        assertTrue(account.createAccount("Alice", "alice@example.com", "password"));

        // Test invalid name
        assertFalse(account.createAccount("", "bob@example.com", "password"));
        assertFalse(account.createAccount(null, "bob@example.com", "password"));

        // Test invalid email
        assertFalse(account.createAccount("Bob", "invalid-email", "password"));
        assertFalse(account.createAccount("Bob", "", "password"));
        assertFalse(account.createAccount("Bob", null, "password"));

        // Test invalid password
        assertFalse(account.createAccount("Charlie", "charlie@example.com", ""));
        assertFalse(account.createAccount("Charlie", "charlie@example.com", "pass"));
        assertFalse(account.createAccount("Charlie", "charlie@example.com", null));

    }
}

