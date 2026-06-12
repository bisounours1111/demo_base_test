package com.example.demoapi.exception;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(Long id) {
        super("No ticket found with id " + id);
    }
}
