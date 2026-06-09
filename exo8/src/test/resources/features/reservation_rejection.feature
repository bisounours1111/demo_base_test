Feature: Refus d'une réservation invalide

  Scenario: Réservation refusée si salle inconnue
    Given une salle inconnue avec le code "INCONNUE"
    When l'utilisateur "user@test.com" réserve la salle "INCONNUE" pour 5 participants du "2026-06-10T10:00" au "2026-06-10T12:00"
    Then la réservation est refusée avec le message "Salle introuvable"

  Scenario: Réservation refusée si capacité insuffisante
    Given une salle avec le code "SALLE-D", le nom "Salle Delta" et une capacité de 5
    When l'utilisateur "user@test.com" réserve la salle "SALLE-D" pour 10 participants du "2026-06-10T10:00" au "2026-06-10T12:00"
    Then la réservation est refusée avec le message "Capacité insuffisante"

  Scenario: Réservation refusée si période invalide
    Given une salle avec le code "SALLE-E", le nom "Salle Epsilon" et une capacité de 10
    When l'utilisateur "user@test.com" réserve la salle "SALLE-E" pour 5 participants du "2026-06-10T12:00" au "2026-06-10T10:00"
    Then la réservation est refusée avec le message "Période invalide"

  Scenario: Réservation refusée si conflit de réservation
    Given une salle avec le code "SALLE-F", le nom "Salle Zeta" et une capacité de 10
    And une réservation existante en conflit pour la salle "SALLE-F" du "2026-06-10T10:00" au "2026-06-10T12:00"
    When l'utilisateur "user@test.com" réserve la salle "SALLE-F" pour 5 participants du "2026-06-10T11:00" au "2026-06-10T13:00"
    Then la réservation est refusée avec le message "Conflit de réservation"
