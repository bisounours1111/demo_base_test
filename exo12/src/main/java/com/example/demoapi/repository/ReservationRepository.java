package com.example.demoapi.repository;

import com.example.demoapi.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    List<Reservation> findConfirmedByRoomId(Long roomId);

    void update(Reservation reservation);

    void deleteAll();
}
