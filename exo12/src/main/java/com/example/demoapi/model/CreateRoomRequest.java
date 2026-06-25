package com.example.demoapi.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateRoomRequest(
        @NotBlank(message = "Name is required")
        String name,

        @Min(value = 1, message = "Capacity must be greater than or equal to 1")
        int capacity
) {
}
