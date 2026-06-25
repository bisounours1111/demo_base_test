package com.example.demoapi.repository;

import com.example.demoapi.model.Reservation;
import com.example.demoapi.model.ReservationStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryReservationRepository implements ReservationRepository {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();

    @Override
    public Reservation save(Reservation reservation) {
        Long id = sequence.incrementAndGet();
        Reservation saved = new Reservation(
                id,
                reservation.roomId(),
                reservation.reservedBy(),
                reservation.startTime(),
                reservation.endTime(),
                reservation.status()
        );
        reservations.put(id, saved);
        return saved;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public List<Reservation> findConfirmedByRoomId(Long roomId) {
        return new ArrayList<>(reservations.values())
                .stream()
                .filter(reservation -> reservation.roomId().equals(roomId))
                .filter(reservation -> reservation.status() == ReservationStatus.CONFIRMED)
                .sorted(Comparator.comparing(Reservation::id))
                .toList();
    }

    @Override
    public void update(Reservation reservation) {
        reservations.put(reservation.id(), reservation);
    }

    @Override
    public void deleteAll() {
        reservations.clear();
        sequence.set(0);
    }
}
