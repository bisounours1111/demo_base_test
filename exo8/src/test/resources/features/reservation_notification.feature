Feature: Notification de confirmation

  Scenario: Notification envoyée en cas de succès
    Given une salle avec le code "SALLE-G", le nom "Salle Eta" et une capacité de 10
    When l'utilisateur "user@test.com" réserve la salle "SALLE-G" pour 3 participants du "2026-06-10T10:00" au "2026-06-10T12:00"
    Then une confirmation est envoyée à "user@test.com"

  Scenario: Notification non envoyée en cas d'échec
    Given une salle inconnue avec le code "INCONNUE-2"
    When l'utilisateur "user@test.com" réserve la salle "INCONNUE-2" pour 3 participants du "2026-06-10T10:00" au "2026-06-10T12:00"
    Then aucune confirmation n'est envoyée
