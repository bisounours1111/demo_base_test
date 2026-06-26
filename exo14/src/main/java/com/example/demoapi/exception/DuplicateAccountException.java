package com.example.demoapi.exception;

public class DuplicateAccountException extends RuntimeException {

    public DuplicateAccountException(String number) {
        super("Account already exists with number " + number);
    }
}
