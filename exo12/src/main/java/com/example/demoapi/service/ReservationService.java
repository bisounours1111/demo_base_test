package com.example.demoapi.service;

import com.example.demoapi.exception.InvalidTimeSlotException;
import com.example.demoapi.exception.ReservationConflictException;
import com.example.demoapi.exception.ReservationNotFoundException;
import com.example.demoapi.exception.RoomNotFoundException;
import com.example.demoapi.model.Reservation;
import com.example.demoapi.model.ReservationStatus;
import com.example.demoapi.repository.ReservationRepository;
import com.example.demoapi.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public Reservation create(Long roomId, String reservedBy, Instant startTime, Instant endTime) {
        if (reservedBy == null || reservedBy.isBlank()) {
            throw new IllegalArgumentException("Reserved by is required");
        }
        if (!startTime.isBefore(endTime)) {
            throw new InvalidTimeSlotException();
        }

        roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));

        for (Reservation existing : reservationRepository.findConfirmedByRoomId(roomId)) {
            if (overlaps(startTime, endTime, existing.startTime(), existing.endTime())) {
                throw new ReservationConflictException("Time slot overlaps with an existing reservation");
            }
        }

        var reservation = new Reservation(
                null,
                roomId,
                reservedBy.trim(),
                startTime,
                endTime,
                ReservationStatus.CONFIRMED
        );

        return reservationRepository.save(reservation);
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public Reservation cancel(Long id) {
        var reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        if (reservation.status() == ReservationStatus.CANCELLED) {
            throw new ReservationConflictException("Reservation is already cancelled");
        }

        var cancelled = new Reservation(
                reservation.id(),
                reservation.roomId(),
                reservation.reservedBy(),
                reservation.startTime(),
                reservation.endTime(),
                ReservationStatus.CANCELLED
        );
        reservationRepository.update(cancelled);
        return cancelled;
    }

    private boolean overlaps(Instant start1, Instant end1, Instant start2, Instant end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
