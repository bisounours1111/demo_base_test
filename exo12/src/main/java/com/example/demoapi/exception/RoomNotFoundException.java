package com.example.demoapi.exception;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(Long id) {
        super("No room found with id " + id);
    }
}
