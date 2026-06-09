package com.example.exception;

public class ReservationRefusedException extends RuntimeException {

    public ReservationRefusedException(String message) {
        super(message);
    }
}
