package com.example.demoapi.model;

import java.time.Instant;

public record ReservationResponse(
        Long id,
        Long roomId,
        String reservedBy,
        Instant startTime,
        Instant endTime,
        ReservationStatus status
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.roomId(),
                reservation.reservedBy(),
                reservation.startTime(),
                reservation.endTime(),
                reservation.status()
        );
    }
}
