package com.example.demoapi.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long id) {
        super("No reservation found with id " + id);
    }
}
