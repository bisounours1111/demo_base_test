package com.example.demoapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 3, message = "Title must contain at least 3 characters")
        String title,

        @NotNull(message = "Priority is required")
        Priority priority
) {
}
