package com.example.demoapi.exception;

import com.example.demoapi.model.TicketStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(TicketStatus currentStatus, TicketStatus requestedStatus) {
        super("Forbidden status transition: " + currentStatus + " to " + requestedStatus);
    }
}
