Feature: Acceptation d'une réservation de salle

  Scenario: Réservation acceptée
    Given une salle avec le code "SALLE-A", le nom "Salle Alpha" et une capacité de 10
    When l'utilisateur "user@test.com" réserve la salle "SALLE-A" pour 5 participants du "2026-06-10T10:00" au "2026-06-10T12:00"
    Then la réservation est acceptée

  Scenario: Réservation acceptée à capacité maximale
    Given une salle avec le code "SALLE-B", le nom "Salle Beta" et une capacité de 8
    When l'utilisateur "user@test.com" réserve la salle "SALLE-B" pour 8 participants du "2026-06-10T14:00" au "2026-06-10T16:00"
    Then la réservation est acceptée

  Scenario: Réservation acceptée si le créneau commence après une réservation existante
    Given une salle avec le code "SALLE-C", le nom "Salle Gamma" et une capacité de 20
    And une réservation existante sans conflit pour la salle "SALLE-C" du "2026-06-10T10:00" au "2026-06-10T12:00"
    When l'utilisateur "user@test.com" réserve la salle "SALLE-C" pour 10 participants du "2026-06-10T12:00" au "2026-06-10T14:00"
    Then la réservation est acceptée
