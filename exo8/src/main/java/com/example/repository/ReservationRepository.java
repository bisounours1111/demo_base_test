package com.example.repository;

import com.example.domain.Reservation;

import java.time.LocalDateTime;

public interface ReservationRepository {
    boolean hasOverlappingReservation(String roomCode, LocalDateTime startDate, LocalDateTime endDate);

    void save(Reservation reservation);
}
