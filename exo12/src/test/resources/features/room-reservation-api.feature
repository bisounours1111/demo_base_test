Feature: Room reservation API
  This feature describes the expected room reservation REST API behavior.
  The vocabulary stays simple to focus on Given / When / Then.

  Scenario: Reservation accepted when room exists and slot is free
    Given no room or reservation exists in the API
    And a room "Room A" with capacity 10 exists in the API
    When I create a reservation for room 1 by "Alice" from "2026-06-25T10:00:00Z" to "2026-06-25T11:00:00Z"
    Then the HTTP response should be 201
    And the response contains reservedBy "Alice"
    And the response contains status "CONFIRMED"

  Scenario: Reservation rejected when room does not exist
    Given no room or reservation exists in the API
    When I create a reservation for room 99 by "Alice" from "2026-06-25T10:00:00Z" to "2026-06-25T11:00:00Z"
    Then the HTTP response should be 404
    And the response contains an error message

  Scenario: Reservation rejected when slot overlaps an existing reservation
    Given no room or reservation exists in the API
    And a room "Room A" with capacity 10 exists in the API
    And a confirmed reservation exists for room 1 by "Bob" from "2026-06-25T10:30:00Z" to "2026-06-25T11:30:00Z"
    When I create a reservation for room 1 by "Alice" from "2026-06-25T10:00:00Z" to "2026-06-25T11:00:00Z"
    Then the HTTP response should be 409
    And the response contains an error message
