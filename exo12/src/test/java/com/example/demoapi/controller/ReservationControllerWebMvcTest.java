package com.example.demoapi.controller;

import com.example.demoapi.exception.ReservationConflictException;
import com.example.demoapi.exception.ReservationNotFoundException;
import com.example.demoapi.exception.RoomNotFoundException;
import com.example.demoapi.model.Reservation;
import com.example.demoapi.model.ReservationStatus;
import com.example.demoapi.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerWebMvcTest {

    private static final Instant START = Instant.parse("2026-06-25T10:00:00Z");
    private static final Instant END = Instant.parse("2026-06-25T11:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService service;

    @Test
    void shouldReturnCreated_whenPostBodyIsValid() throws Exception {
        // Arrange
        when(service.create(eq(1L), eq("Alice"), eq(START), eq(END)))
                .thenReturn(new Reservation(1L, 1L, "Alice", START, END, ReservationStatus.CONFIRMED));

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "roomId": 1,
                                  "reservedBy": "Alice",
                                  "startTime": "2026-06-25T10:00:00Z",
                                  "endTime": "2026-06-25T11:00:00Z"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/reservations/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roomId").value(1))
                .andExpect(jsonPath("$.reservedBy").value("Alice"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(service).create(1L, "Alice", START, END);
    }

    @Test
    void shouldReturnNotFound_whenReservationDoesNotExist() throws Exception {
        // Arrange
        when(service.getById(99L)).thenThrow(new ReservationNotFoundException(99L));

        // Act + Assert
        mockMvc.perform(get("/api/reservations/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No reservation found with id 99"));

        verify(service).getById(99L);
    }

    @Test
    void shouldReturnNotFound_whenRoomDoesNotExistOnCreate() throws Exception {
        // Arrange
        when(service.create(eq(99L), eq("Alice"), eq(START), eq(END)))
                .thenThrow(new RoomNotFoundException(99L));

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "roomId": 99,
                                  "reservedBy": "Alice",
                                  "startTime": "2026-06-25T10:00:00Z",
                                  "endTime": "2026-06-25T11:00:00Z"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No room found with id 99"));
    }

    @Test
    void shouldReturnConflict_whenSlotOverlapsExistingReservation() throws Exception {
        // Arrange
        when(service.create(eq(1L), eq("Alice"), eq(START), eq(END)))
                .thenThrow(new ReservationConflictException("Time slot overlaps with an existing reservation"));

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "roomId": 1,
                                  "reservedBy": "Alice",
                                  "startTime": "2026-06-25T10:00:00Z",
                                  "endTime": "2026-06-25T11:00:00Z"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Time slot overlaps with an existing reservation"));
    }

    @Test
    void shouldReturnConflict_whenCancellingAlreadyCancelledReservation() throws Exception {
        // Arrange
        when(service.cancel(1L))
                .thenThrow(new ReservationConflictException("Reservation is already cancelled"));

        // Act + Assert
        mockMvc.perform(patch("/api/reservations/1/cancel"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Reservation is already cancelled"));

        verify(service).cancel(1L);
    }
}
