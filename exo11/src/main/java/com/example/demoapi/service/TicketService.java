package com.example.demoapi.service;

import com.example.demoapi.exception.NotImplementedException;
import com.example.demoapi.model.Priority;
import com.example.demoapi.model.Ticket;
import com.example.demoapi.model.TicketStatus;
import com.example.demoapi.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket create(String title, Priority priority) {
        throw new NotImplementedException("Ticket creation is not implemented yet");
    }

    public Ticket getById(Long id) {
        throw new NotImplementedException("Ticket retrieval is not implemented yet");
    }

    public List<Ticket> findAll() {
        throw new NotImplementedException("Ticket listing is not implemented yet");
    }

    public Ticket updateStatus(Long id, TicketStatus status) {
        throw new NotImplementedException("Status update is not implemented yet");
    }

    public void deleteAll() {
        throw new NotImplementedException("Ticket deletion is not implemented yet");
    }
}
