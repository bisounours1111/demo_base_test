package com.example.demoapi.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String number) {
        super("No account found with number " + number);
    }
}
