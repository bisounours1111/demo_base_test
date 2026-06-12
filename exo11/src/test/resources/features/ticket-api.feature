Feature: Support ticket API
  This feature describes the expected ticket REST API behavior.
  The vocabulary stays simple to focus on Given / When / Then.

  Scenario: Valid ticket creation
    Given no ticket exists in the API
    When I create a ticket with title "bdd demo" and priority "HIGH"
    Then the HTTP response should be 201
    And the response contains title "bdd demo"

  Scenario: Retrieve a created ticket
    Given no ticket exists in the API
    When I create a ticket with title "ticket to find" and priority "MEDIUM"
    And I request the created ticket
    Then the HTTP response should be 200
    And the response contains title "ticket to find"

  Scenario: Reject invalid creation
    Given no ticket exists in the API
    When I create a ticket with title "ab" and priority "HIGH"
    Then the HTTP response should be 400
    And the response contains an error message

  Scenario: Reject creation without priority
    Given no ticket exists in the API
    When I create a ticket with title "Valid title" and no priority
    Then the HTTP response should be 400
    And the response contains an error message

  Scenario: Ticket not found
    Given no ticket exists in the API
    When I request the ticket with id 99
    Then the HTTP response should be 404
    And the response contains an error message

  Scenario: Resolve a ticket
    Given no ticket exists in the API
    When I create a ticket with title "Application bug" and priority "MEDIUM"
    And I update the created ticket status to "RESOLVED"
    Then the HTTP response should be 200
    And the response contains status "RESOLVED"

  Scenario: Reject update on already resolved ticket
    Given no ticket exists in the API
    When I create a ticket with title "Closed ticket" and priority "LOW"
    And I update the created ticket status to "RESOLVED"
    And I update the created ticket status to "IN_PROGRESS"
    Then the HTTP response should be 409
    And the response contains an error message

  Scenario Outline: Ticket creation with multiple data sets
    Given no ticket exists in the API
    When I create a ticket with title "<title>" and priority "<priority>"
    Then the HTTP response should be <status>

    Examples:
      | title        | priority | status |
      | Valid ticket | LOW      | 201    |
      | ab           | HIGH     | 400    |

  Scenario: Retrieve a ticket list prepared with a table
    Given no ticket exists in the API
    And the following tickets exist in the API
      | title         | priority |
      | first ticket  | LOW      |
      | second ticket | MEDIUM   |
      | third ticket  | HIGH     |
    When I request the ticket list
    Then the HTTP response should be 200
    And the response contains 3 tickets
    And the response contains at least title "first ticket"
    And the response contains at least title "second ticket"
    And the response contains at least title "third ticket"
