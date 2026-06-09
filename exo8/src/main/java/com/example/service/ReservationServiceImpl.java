package com.example.service;

import com.example.domain.Reservation;
import com.example.domain.ReservationConfirmation;
import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;

public class ReservationServiceImpl implements ReservationService {

    public ReservationServiceImpl(RoomRepository roomRepository,
                                  ReservationRepository reservationRepository,
                                  NotificationService notificationService) {
    }

    @Override
    public ReservationConfirmation reserve(Reservation reservation) {
    }
}
