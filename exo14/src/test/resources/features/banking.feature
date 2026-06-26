Feature: Bank account management
  As a banking API user
  I want to manage my accounts and perform financial operations
  So that I can track and handle my funds safely

  Scenario: Create a new account with initial balance of zero
    Given no account exists in the API
    When I create account "ACC001" for "Alice Dupont"
    Then the HTTP response should be 201
    And account "ACC001" should belong to "Alice Dupont"
    And account "ACC001" should have a balance of 0

  Scenario: Successful deposit
    Given no account exists in the API
    And account "ACC001" owned by "Alice Dupont" exists with a balance of 100
    When I deposit 50 on account "ACC001"
    Then the HTTP response should be 200
    And account "ACC001" should have a balance of 150

  Scenario: Successful withdrawal with sufficient funds
    Given no account exists in the API
    And account "ACC001" owned by "Alice Dupont" exists with a balance of 200
    When I withdraw 50 from account "ACC001"
    Then the HTTP response should be 200
    And account "ACC001" should have a balance of 150

  Scenario: Withdrawal rejected due to insufficient funds
    Given no account exists in the API
    And account "ACC001" owned by "Alice Dupont" exists with a balance of 30
    When I withdraw 50 from account "ACC001"
    Then the HTTP response should be 400
    And account "ACC001" should have a balance of 30
    And the response should contain an error message

  Scenario: Successful transfer between two accounts
    Given no account exists in the API
    And account "ACC001" owned by "Alice Dupont" exists with a balance of 200
    And account "ACC002" owned by "Bob Martin" exists with a balance of 50
    When I transfer 75 from account "ACC001" to account "ACC002"
    Then the HTTP response should be 204
    And account "ACC001" should have a balance of 125
    And account "ACC002" should have a balance of 125

  Scenario: Transfer rejected due to insufficient sender balance
    Given no account exists in the API
    And account "ACC001" owned by "Alice Dupont" exists with a balance of 40
    And account "ACC002" owned by "Bob Martin" exists with a balance of 100
    When I transfer 100 from account "ACC001" to account "ACC002"
    Then the HTTP response should be 400
    And account "ACC001" should have a balance of 40
    And account "ACC002" should have a balance of 100
    And the response should contain an error message
