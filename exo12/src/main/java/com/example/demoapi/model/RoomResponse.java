package com.example.demoapi.model;

public record RoomResponse(Long id, String name, int capacity) {

    public static RoomResponse from(Room room) {
        return new RoomResponse(room.id(), room.name(), room.capacity());
    }
}
