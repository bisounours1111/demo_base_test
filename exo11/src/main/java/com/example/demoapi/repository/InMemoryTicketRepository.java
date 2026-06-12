package com.example.demoapi.repository;

import com.example.demoapi.exception.NotImplementedException;
import com.example.demoapi.model.Priority;
import com.example.demoapi.model.Ticket;
import com.example.demoapi.model.TicketStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryTicketRepository implements TicketRepository {

    @Override
    public Ticket save(String title, Priority priority, TicketStatus status) {
        throw new NotImplementedException("Ticket save is not implemented yet");
    }

    @Override
    public void update(Ticket ticket) {
        throw new NotImplementedException("Ticket update is not implemented yet");
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        throw new NotImplementedException("Ticket findById is not implemented yet");
    }

    @Override
    public List<Ticket> findAll() {
        throw new NotImplementedException("Ticket findAll is not implemented yet");
    }

    @Override
    public void deleteAll() {
        throw new NotImplementedException("Ticket deleteAll is not implemented yet");
    }
}
