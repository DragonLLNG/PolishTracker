package com.example.nailpolishapp;

import java.util.ArrayList;
import java.util.List;

public class CreateAccount {

    private final List<UserMock> users = new ArrayList<>();

    public boolean createAccount(String name, String email, String password) {
        if (!isValidName(name) || !isValidEmail(email) || !isValidPassword(password)) {
            return false;
        }

        UserMock newUser = new UserMock(name, email, password);


        users.add(newUser);
        return true;
    }

    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        // Check that the email contains an "@" symbol
        int atSymbolIndex = email.indexOf('@');
        if (atSymbolIndex == -1) {
            return false;
        }

        // Check that the email contains at least one character before and after the "@" symbol
        if (atSymbolIndex == 0 || atSymbolIndex == email.length() - 1) {
            return false;
        }

        // Check that the email contains a "." symbol after the "@" symbol
        int dotSymbolIndex = email.indexOf('.', atSymbolIndex);
        if (dotSymbolIndex == -1 || dotSymbolIndex == email.length() - 1) {
            return false;
        }

        // Check that the email domain contains at least one character before and after the "." symbol
        return dotSymbolIndex != atSymbolIndex + 1 && dotSymbolIndex != email.length() - 2;

    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }



}


