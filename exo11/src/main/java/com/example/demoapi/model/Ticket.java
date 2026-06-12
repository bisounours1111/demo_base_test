package com.example.demoapi.model;

public record Ticket(Long id, String title, Priority priority, TicketStatus status) {
}
