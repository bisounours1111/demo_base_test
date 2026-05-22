package com.example;

public class PasswordValidator {

    public boolean isValid(String password) {
        return getErrorMessage(password).equals("Password is valid");
    }

    public String getErrorMessage(String password) {

        if (password == null) {
            return "Password must not be null";
        }

        if (password.length() < 8) {
            return "Password must contain at least 8 characters";
        }

        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }

        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }

        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit";
        }

        if (!password.matches(".*[_!@#$%].*")) {
            return "Password must contain at least one special character";
        }

        return "Password is valid";
    }
}