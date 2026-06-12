package com.example.demoapi.service;

import com.example.demoapi.exception.InvalidStatusTransitionException;
import com.example.demoapi.exception.TicketNotFoundException;
import com.example.demoapi.model.Priority;
import com.example.demoapi.model.Ticket;
import com.example.demoapi.model.TicketStatus;
import com.example.demoapi.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TicketService {

    private static final Map<TicketStatus, Set<TicketStatus>> ALLOWED_TRANSITIONS = Map.of(
            TicketStatus.OPEN, Set.of(TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED),
            TicketStatus.IN_PROGRESS, Set.of(TicketStatus.RESOLVED)
    );

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket create(String title, Priority priority) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        var trimmedTitle = title.trim();
        if (trimmedTitle.length() < 3) {
            throw new IllegalArgumentException("Title must contain at least 3 characters");
        }

        return repository.save(trimmedTitle, priority, TicketStatus.OPEN);
    }

    public Ticket getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    public List<Ticket> findAll() {
        return repository.findAll();
    }

    public Ticket updateStatus(Long id, TicketStatus status) {
        var ticket = repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        if (!ALLOWED_TRANSITIONS.getOrDefault(ticket.status(), Set.of()).contains(status)) {
            throw new InvalidStatusTransitionException(ticket.status(), status);
        }

        var updated = new Ticket(ticket.id(), ticket.title(), ticket.priority(), status);
        repository.update(updated);
        return updated;
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
