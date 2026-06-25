package com.example.demoapi.service;

import com.example.demoapi.model.Room;
import com.example.demoapi.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Room create(String name, int capacity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (capacity < 1) {
            throw new IllegalArgumentException("Capacity must be greater than or equal to 1");
        }

        return repository.save(name.trim(), capacity);
    }

    public List<Room> findAll() {
        return repository.findAll();
    }
}
