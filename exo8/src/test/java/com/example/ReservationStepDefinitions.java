package com.example;

import com.example.domain.Reservation;
import com.example.domain.ReservationConfirmation;
import com.example.domain.Room;
import com.example.exception.ReservationRefusedException;
import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;
import com.example.service.NotificationService;
import com.example.service.ReservationService;
import com.example.service.ReservationServiceImpl;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ReservationStepDefinitions {

    private final RoomRepository roomRepositoryMock = Mockito.mock(RoomRepository.class);
    private final ReservationRepository reservationRepositoryMock = Mockito.mock(ReservationRepository.class);
    private final NotificationService notificationServiceMock = Mockito.mock(NotificationService.class);
    private final ReservationService reservationService = new ReservationServiceImpl(
            roomRepositoryMock,
            reservationRepositoryMock,
            notificationServiceMock
    );

    private ReservationConfirmation confirmation;
    private Exception thrownException;
    private String lastUserEmail;
    private String lastRoomCode;

    @Before
    public void reset() {
        Mockito.reset(roomRepositoryMock, reservationRepositoryMock, notificationServiceMock);
        confirmation = null;
        thrownException = null;
        lastUserEmail = null;
        lastRoomCode = null;
        Mockito.when(reservationRepositoryMock.hasOverlappingReservation(anyString(), any(), any()))
                .thenReturn(false);
    }

    @Given("une salle avec le code {string}, le nom {string} et une capacité de {int}")
    public void uneSalleExiste(String code, String name, int capacity) {
        Room room = Mockito.mock(Room.class);
        Mockito.when(room.getCode()).thenReturn(code);
        Mockito.when(room.getName()).thenReturn(name);
        Mockito.when(room.getMaxCapacity()).thenReturn(capacity);
        Mockito.when(roomRepositoryMock.findByCode(code)).thenReturn(Optional.of(room));
    }

    @Given("une salle inconnue avec le code {string}")
    public void uneSalleInconnue(String code) {
        Mockito.when(roomRepositoryMock.findByCode(code)).thenReturn(Optional.empty());
    }

    @Given("une réservation existante sans conflit pour la salle {string} du {string} au {string}")
    public void uneReservationSansConflit(String roomCode, String start, String end) {
        Mockito.when(reservationRepositoryMock.hasOverlappingReservation(eq(roomCode), any(), any()))
                .thenReturn(false);
    }

    @Given("une réservation existante en conflit pour la salle {string} du {string} au {string}")
    public void uneReservationEnConflit(String roomCode, String start, String end) {
        Mockito.when(reservationRepositoryMock.hasOverlappingReservation(eq(roomCode), any(), any()))
                .thenReturn(true);
    }

    @When("l'utilisateur {string} réserve la salle {string} pour {int} participants du {string} au {string}")
    public void lUtilisateurReserve(String userEmail, String roomCode, int participants,
                                    String start, String end) {
        lastUserEmail = userEmail;
        lastRoomCode = roomCode;
        Reservation reservation = Mockito.mock(Reservation.class);
        Mockito.when(reservation.getUserEmail()).thenReturn(userEmail);
        Mockito.when(reservation.getRoomCode()).thenReturn(roomCode);
        Mockito.when(reservation.getParticipantCount()).thenReturn(participants);
        Mockito.when(reservation.getStartDate()).thenReturn(LocalDateTime.parse(start));
        Mockito.when(reservation.getEndDate()).thenReturn(LocalDateTime.parse(end));
        try {
            confirmation = reservationService.reserve(reservation);
            thrownException = null;
        } catch (Exception e) {
            thrownException = e;
            confirmation = null;
        }
    }

    @Then("la réservation est acceptée")
    public void laReservationEstAcceptee() {
        assertNull(thrownException, "La réservation a été refusée à tort.");
        assertNotNull(confirmation, "La confirmation n'a pas été générée.");
        assertEquals(lastRoomCode, confirmation.getRoomCode());
        assertEquals(lastUserEmail, confirmation.getUserEmail());
    }

    @Then("la réservation est refusée avec le message {string}")
    public void laReservationEstRefusee(String expectedMessage) {
        assertNull(confirmation, "La réservation a été acceptée à tort.");
        assertNotNull(thrownException, "Aucune exception n'a été levée.");
        assertInstanceOf(ReservationRefusedException.class, thrownException);
        assertEquals(expectedMessage, thrownException.getMessage());
    }

    @Then("une confirmation est envoyée à {string}")
    public void uneConfirmationEstEnvoyee(String email) {
        assertNull(thrownException, "La réservation a été refusée à tort.");
        verify(notificationServiceMock).sendConfirmation(eq(email), anyString());
    }

    @Then("aucune confirmation n'est envoyée")
    public void aucuneConfirmationNestEnvoyee() {
        assertNotNull(thrownException, "La réservation aurait dû être refusée.");
        verify(notificationServiceMock, never()).sendConfirmation(anyString(), anyString());
    }
}
