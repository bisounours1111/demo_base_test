package com.example.service;

import com.example.domain.DefaultReservationConfirmation;
import com.example.domain.Reservation;
import com.example.domain.ReservationConfirmation;
import com.example.domain.Room;
import com.example.exception.ReservationRefusedException;
import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;

public class ReservationServiceImpl implements ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public ReservationServiceImpl(RoomRepository roomRepository,
                                  ReservationRepository reservationRepository,
                                  NotificationService notificationService) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    @Override
    public ReservationConfirmation reserve(Reservation reservation) {
        Room room = roomRepository.findByCode(reservation.getRoomCode())
                .orElseThrow(() -> new ReservationRefusedException("Salle introuvable"));

        if (reservation.getParticipantCount() > room.getMaxCapacity()) {
            throw new ReservationRefusedException("Capacité insuffisante");
        }

        if (!reservation.getEndDate().isAfter(reservation.getStartDate())) {
            throw new ReservationRefusedException("Période invalide");
        }

        if (reservationRepository.hasOverlappingReservation(
                reservation.getRoomCode(),
                reservation.getStartDate(),
                reservation.getEndDate())) {
            throw new ReservationRefusedException("Conflit de réservation");
        }

        reservationRepository.save(reservation);

        String message = "Réservation confirmée pour " + reservation.getUserEmail()
                + " - " + room.getName();
        notificationService.sendConfirmation(reservation.getUserEmail(), message);

        return new DefaultReservationConfirmation(
                reservation.getRoomCode(),
                reservation.getUserEmail(),
                message
        );
    }
}
