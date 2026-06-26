package com.example.demoapi.exception;

import java.math.BigDecimal;

public class InvalidAmountException extends IllegalArgumentException {

    public InvalidAmountException(BigDecimal amount) {
        super("Amount must be strictly positive, got: " + amount);
    }
}
