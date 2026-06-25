package com.example.demoapi.service;

import com.example.demoapi.exception.InvalidTimeSlotException;
import com.example.demoapi.exception.ReservationConflictException;
import com.example.demoapi.exception.ReservationNotFoundException;
import com.example.demoapi.exception.RoomNotFoundException;
import com.example.demoapi.model.Reservation;
import com.example.demoapi.model.ReservationStatus;
import com.example.demoapi.model.Room;
import com.example.demoapi.repository.ReservationRepository;
import com.example.demoapi.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final Instant START = Instant.parse("2026-06-25T10:00:00Z");
    private static final Instant END = Instant.parse("2026-06-25T11:00:00Z");

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService service;

    @Test
    void shouldCreateReservation_whenRoomExistsAndSlotIsValid() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Room A", 10)));
        when(reservationRepository.findConfirmedByRoomId(1L)).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(new Reservation(1L, 1L, "Alice", START, END, ReservationStatus.CONFIRMED));

        // Act
        Reservation result = service.create(1L, "Alice", START, END);

        // Assert
        assertEquals(1L, result.id());
        assertEquals(1L, result.roomId());
        assertEquals("Alice", result.reservedBy());
        assertEquals(ReservationStatus.CONFIRMED, result.status());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void shouldThrowNotFound_whenRoomDoesNotExist() {
        // Arrange
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RoomNotFoundException.class, () -> service.create(99L, "Alice", START, END));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void shouldThrowException_whenEndTimeIsNotAfterStartTime() {
        // Arrange
        Instant invalidEnd = Instant.parse("2026-06-25T10:00:00Z");

        // Act + Assert
        assertThrows(InvalidTimeSlotException.class, () -> service.create(1L, "Alice", START, invalidEnd));
        verify(roomRepository, never()).findById(any());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void shouldThrowException_whenEndTimeIsBeforeStartTime() {
        // Arrange
        Instant invalidEnd = Instant.parse("2026-06-25T09:00:00Z");

        // Act + Assert
        assertThrows(InvalidTimeSlotException.class, () -> service.create(1L, "Alice", START, invalidEnd));
        verify(roomRepository, never()).findById(any());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void shouldThrowConflict_whenSlotOverlapsExistingConfirmedReservation() {
        // Arrange
        var existing = new Reservation(
                1L, 1L, "Bob",
                Instant.parse("2026-06-25T10:30:00Z"),
                Instant.parse("2026-06-25T11:30:00Z"),
                ReservationStatus.CONFIRMED
        );
        when(roomRepository.findById(1L)).thenReturn(Optional.of(new Room(1L, "Room A", 10)));
        when(reservationRepository.findConfirmedByRoomId(1L)).thenReturn(List.of(existing));

        // Act + Assert
        assertThrows(
                ReservationConflictException.class,
                () -> service.create(1L, "Alice", START, END)
        );
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void shouldCancelConfirmedReservation() {
        // Arrange
        var existing = new Reservation(1L, 1L, "Alice", START, END, ReservationStatus.CONFIRMED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existing));

        // Act
        Reservation result = service.cancel(1L);

        // Assert
        assertEquals(ReservationStatus.CANCELLED, result.status());
        verify(reservationRepository).update(any(Reservation.class));
    }

    @Test
    void shouldThrowConflict_whenReservationAlreadyCancelled() {
        // Arrange
        var cancelled = new Reservation(1L, 1L, "Alice", START, END, ReservationStatus.CANCELLED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(cancelled));

        // Act + Assert
        assertThrows(ReservationConflictException.class, () -> service.cancel(1L));
        verify(reservationRepository, never()).update(any(Reservation.class));
    }

    @Test
    void shouldThrowNotFound_whenCancellingUnknownReservation() {
        // Arrange
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ReservationNotFoundException.class, () -> service.cancel(99L));
        verify(reservationRepository, never()).update(any(Reservation.class));
    }

    @Test
    void shouldThrowException_whenReservedByIsBlank() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> service.create(1L, "   ", START, END));
        verify(roomRepository, never()).findById(any());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }
}
