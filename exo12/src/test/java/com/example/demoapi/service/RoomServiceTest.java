package com.example.demoapi.service;

import com.example.demoapi.model.Room;
import com.example.demoapi.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository repository;

    @InjectMocks
    private RoomService service;

    @Test
    void shouldCreateRoom_whenNameAndCapacityAreValid() {
        // Arrange
        when(repository.save("Room A", 10))
                .thenReturn(new Room(1L, "Room A", 10));

        // Act
        Room result = service.create("Room A", 10);

        // Assert
        assertEquals(1L, result.id());
        assertEquals("Room A", result.name());
        assertEquals(10, result.capacity());
        verify(repository).save("Room A", 10);
    }

    @Test
    void shouldTrimName_whenCreatingRoom() {
        // Arrange
        when(repository.save("Room A", 10))
                .thenReturn(new Room(1L, "Room A", 10));

        // Act
        Room result = service.create("  Room A  ", 10);

        // Assert
        assertEquals("Room A", result.name());
        verify(repository).save("Room A", 10);
    }

    @Test
    void shouldThrowException_whenNameIsBlank() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.create("   ", 10));
        verify(repository, never()).save(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    void shouldThrowException_whenNameIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.create(null, 10));
        verify(repository, never()).save(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    void shouldThrowException_whenCapacityIsInvalid() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.create("Room A", 0));
        verify(repository, never()).save(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    void shouldReturnAllRooms_whenFindAllIsCalled() {
        // Arrange
        var rooms = List.of(
                new Room(1L, "Room A", 10),
                new Room(2L, "Room B", 20)
        );
        when(repository.findAll()).thenReturn(rooms);

        // Act
        List<Room> result = service.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Room A", result.get(0).name());
        verify(repository).findAll();
    }
}
