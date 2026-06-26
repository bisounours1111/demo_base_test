package com.example.demoapi.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String accountNumber) {
        super("Insufficient funds on account " + accountNumber);
    }
}
