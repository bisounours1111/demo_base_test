package com.example.demoapi.service;

import com.example.demoapi.exception.InvalidStatusTransitionException;
import com.example.demoapi.exception.TicketNotFoundException;
import com.example.demoapi.model.Priority;
import com.example.demoapi.model.Ticket;
import com.example.demoapi.model.TicketStatus;
import com.example.demoapi.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService service;

    @Test
    void shouldCreateTicket_whenTitleIsValid() {
        when(repository.save("Network issue", Priority.HIGH, TicketStatus.OPEN))
                .thenReturn(new Ticket(1L, "Network issue", Priority.HIGH, TicketStatus.OPEN));

        Ticket result = service.create("Network issue", Priority.HIGH);

        assertEquals(1L, result.id());
        assertEquals("Network issue", result.title());
        assertEquals(Priority.HIGH, result.priority());
        verify(repository).save("Network issue", Priority.HIGH, TicketStatus.OPEN);
    }

    @Test
    void shouldTrimTitle_whenCreatingTicket() {
        when(repository.save("Network issue", Priority.HIGH, TicketStatus.OPEN))
                .thenReturn(new Ticket(1L, "Network issue", Priority.HIGH, TicketStatus.OPEN));

        Ticket result = service.create("  Network issue  ", Priority.HIGH);

        assertEquals("Network issue", result.title());
        verify(repository).save("Network issue", Priority.HIGH, TicketStatus.OPEN);
    }

    @Test
    void shouldCreateTicketWithOpenStatus_whenTicketIsCreated() {
        when(repository.save("Open ticket", Priority.LOW, TicketStatus.OPEN))
                .thenReturn(new Ticket(1L, "Open ticket", Priority.LOW, TicketStatus.OPEN));

        Ticket result = service.create("Open ticket", Priority.LOW);

        assertEquals(TicketStatus.OPEN, result.status());
        verify(repository).save("Open ticket", Priority.LOW, TicketStatus.OPEN);
    }

    @Test
    void shouldThrowException_whenTitleIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> service.create("   ", Priority.HIGH));
        verify(repository, never()).save(any(), any(), any());
    }

    @Test
    void shouldReturnTicket_whenIdExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(
                new Ticket(1L, "Existing ticket", Priority.MEDIUM, TicketStatus.OPEN)
        ));

        Ticket result = service.getById(1L);

        assertEquals("Existing ticket", result.title());
        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowNotFoundException_whenIdDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> service.getById(99L));
        verify(repository).findById(99L);
    }

    @Test
    void shouldAllowTransitionFromOpenToInProgress() {
        var existingTicket = new Ticket(1L, "Ticket", Priority.HIGH, TicketStatus.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(existingTicket));

        Ticket result = service.updateStatus(1L, TicketStatus.IN_PROGRESS);

        assertEquals(TicketStatus.IN_PROGRESS, result.status());
        verify(repository).update(any(Ticket.class));
    }

    @Test
    void shouldAllowTransitionFromOpenToResolved() {
        var existingTicket = new Ticket(1L, "Ticket", Priority.HIGH, TicketStatus.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(existingTicket));

        Ticket result = service.updateStatus(1L, TicketStatus.RESOLVED);

        assertEquals(TicketStatus.RESOLVED, result.status());
        verify(repository).update(any(Ticket.class));
    }

    @Test
    void shouldAllowTransitionFromInProgressToResolved() {
        var existingTicket = new Ticket(1L, "Ticket", Priority.HIGH, TicketStatus.IN_PROGRESS);
        when(repository.findById(1L)).thenReturn(Optional.of(existingTicket));

        Ticket result = service.updateStatus(1L, TicketStatus.RESOLVED);

        assertEquals(TicketStatus.RESOLVED, result.status());
        verify(repository).update(any(Ticket.class));
    }

    @Test
    void shouldRejectTransition_whenTicketIsAlreadyResolved() {
        var existingTicket = new Ticket(1L, "Closed ticket", Priority.LOW, TicketStatus.RESOLVED);
        when(repository.findById(1L)).thenReturn(Optional.of(existingTicket));

        assertThrows(
                InvalidStatusTransitionException.class,
                () -> service.updateStatus(1L, TicketStatus.IN_PROGRESS)
        );
        verify(repository, never()).update(any(Ticket.class));
    }

    @Test
    void shouldRejectTransition_whenTransitionIsForbidden() {
        var existingTicket = new Ticket(1L, "Ticket", Priority.MEDIUM, TicketStatus.IN_PROGRESS);
        when(repository.findById(1L)).thenReturn(Optional.of(existingTicket));

        assertThrows(
                InvalidStatusTransitionException.class,
                () -> service.updateStatus(1L, TicketStatus.OPEN)
        );
        verify(repository, never()).update(any(Ticket.class));
    }
}
