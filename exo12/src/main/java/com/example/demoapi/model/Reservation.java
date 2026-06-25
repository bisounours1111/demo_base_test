package com.example.demoapi.model;

import java.time.Instant;

public record Reservation(
        Long id,
        Long roomId,
        String reservedBy,
        Instant startTime,
        Instant endTime,
        ReservationStatus status
) {
}
