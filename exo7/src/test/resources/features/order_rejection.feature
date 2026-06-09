Feature: Rejection of invalid orders

  Scenario: Order refused if product is unknown
    Given an unknown product with reference "REF-UNKNOWN"
    And a customer with profile "STANDARD"
    When the customer orders a quantity of 1 for reference "REF-UNKNOWN"
    Then the order is refused with the message "Product not found"

  Scenario: Order refused if stock is insufficient
    Given a product with reference "REF-004", a price of 10.0 and a stock of 2
    And a customer with profile "STANDARD"
    When the customer orders a quantity of 5 for reference "REF-004"
    Then the order is refused with the message "Insufficient stock"