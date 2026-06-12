package com.example.demoapi.repository;

import com.example.demoapi.model.Priority;
import com.example.demoapi.model.Ticket;
import com.example.demoapi.model.TicketStatus;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {

    Ticket save(String title, Priority priority, TicketStatus status);

    void update(Ticket ticket);

    Optional<Ticket> findById(Long id);

    List<Ticket> findAll();

    void deleteAll();
}
