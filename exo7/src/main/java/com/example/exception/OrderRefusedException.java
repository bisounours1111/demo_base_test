package com.example.exception;

public class OrderRefusedException extends RuntimeException {
    public OrderRefusedException(String message) {
        super(message);
    }
}