package com.example.domain;

import java.time.LocalDateTime;

public interface Reservation {

    String getUserEmail();

    String getRoomCode();

    int getParticipantCount();

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();
}
