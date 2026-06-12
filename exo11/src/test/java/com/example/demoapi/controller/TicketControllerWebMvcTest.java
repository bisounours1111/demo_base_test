package com.example.demoapi.controller;

import com.example.demoapi.exception.InvalidStatusTransitionException;
import com.example.demoapi.exception.TicketNotFoundException;
import com.example.demoapi.model.Priority;
import com.example.demoapi.model.Ticket;
import com.example.demoapi.model.TicketStatus;
import com.example.demoapi.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService service;

    @Test
    void shouldReturnCreated_whenPostBodyIsValid() throws Exception {
        // Arrange
        when(service.create("Network issue", Priority.HIGH))
                .thenReturn(new Ticket(1L, "Network issue", Priority.HIGH, TicketStatus.OPEN));

        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Network issue\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tickets/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Network issue"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(service).create("Network issue", Priority.HIGH);
    }

    @Test
    void shouldReturnBadRequest_whenPostBodyIsInvalid() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"ab\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Title must contain at least 3 characters"));

        verify(service, never()).create(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldReturnBadRequest_whenPriorityIsMissing() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Valid title\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Priority is required"));

        verify(service, never()).create(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldReturnOk_whenTicketExists() throws Exception {
        // Arrange
        when(service.getById(1L))
                .thenReturn(new Ticket(1L, "Existing ticket", Priority.MEDIUM, TicketStatus.OPEN));

        // Act + Assert
        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Existing ticket"));

        verify(service).getById(1L);
    }

    @Test
    void shouldReturnNotFound_whenTicketDoesNotExist() throws Exception {
        // Arrange
        when(service.getById(99L)).thenThrow(new TicketNotFoundException(99L));

        // Act + Assert
        mockMvc.perform(get("/api/tickets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No ticket found with id 99"));

        verify(service).getById(99L);
    }

    @Test
    void shouldReturnConflict_whenStatusTransitionIsForbidden() throws Exception {
        // Arrange
        when(service.updateStatus(eq(1L), eq(TicketStatus.IN_PROGRESS)))
                .thenThrow(new InvalidStatusTransitionException(TicketStatus.RESOLVED, TicketStatus.IN_PROGRESS));

        // Act + Assert
        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").exists());

        verify(service).updateStatus(1L, TicketStatus.IN_PROGRESS);
    }
}
