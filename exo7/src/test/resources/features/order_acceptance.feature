Feature: Order acceptance and discount calculation

  Scenario: Order accepted for a STANDARD customer
    Given a product with reference "REF-001", a price of 10.0 and a stock of 10
    And a customer with profile "STANDARD"
    When the customer orders a quantity of 2 for reference "REF-001"
    Then the order is accepted and the receipt shows a total amount of 20.0

  Scenario: Order accepted for a PREMIUM customer
    Given a product with reference "REF-002", a price of 100.0 and a stock of 5
    And a customer with profile "PREMIUM"
    When the customer orders a quantity of 1 for reference "REF-002"
    Then the order is accepted and the receipt shows a total amount of 90.0

  Scenario: Order accepted for a VIP customer
    Given a product with reference "REF-003", a price of 50.0 and a stock of 20
    And a customer with profile "VIP"
    When the customer orders a quantity of 2 for reference "REF-003"
    Then the order is accepted and the receipt shows a total amount of 80.0