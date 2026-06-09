package com.example.service;

import com.example.domain.Reservation;
import com.example.domain.ReservationConfirmation;

public interface ReservationService {

    ReservationConfirmation reserve(Reservation reservation);
}
