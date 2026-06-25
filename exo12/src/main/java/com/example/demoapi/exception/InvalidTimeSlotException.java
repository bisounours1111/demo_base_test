package com.example.demoapi.exception;

public class InvalidTimeSlotException extends RuntimeException {

    public InvalidTimeSlotException() {
        super("End time must be strictly after start time");
    }
}
