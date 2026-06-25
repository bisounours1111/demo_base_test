package com.example.demoapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateReservationRequest(
        @NotNull(message = "Room id is required")
        Long roomId,

        @NotBlank(message = "Reserved by is required")
        String reservedBy,

        @NotNull(message = "Start time is required")
        Instant startTime,

        @NotNull(message = "End time is required")
        Instant endTime
) {
}
