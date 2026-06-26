package com.example.demoapi.model;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(
        @NotBlank(message = "Account number is required")
        String number,

        @NotBlank(message = "Holder name is required")
        String holder
) {
}
