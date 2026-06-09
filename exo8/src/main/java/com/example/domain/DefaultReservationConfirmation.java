package com.example.domain;

public class DefaultReservationConfirmation implements ReservationConfirmation {

    private final String roomCode;
    private final String userEmail;
    private final String confirmationMessage;

    public DefaultReservationConfirmation(String roomCode, String userEmail, String confirmationMessage) {
        this.roomCode = roomCode;
        this.userEmail = userEmail;
        this.confirmationMessage = confirmationMessage;
    }

    @Override
    public String getRoomCode() {
        return roomCode;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public String getConfirmationMessage() {
        return confirmationMessage;
    }
}
