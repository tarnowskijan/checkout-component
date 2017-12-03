Feature: Checkout

  Scenario: Client retrieves receipt for items in shopping cart
    Given the client has shopping cart ID
    Given the client has - 4 coffee - in shopping cart
    Given the client has - 6 milk - in shopping cart
    Given the client has - 1 water - in shopping cart
    When the client calls /shopping/checkouts/{cartId} using GET method
    Then the client receives receipt containing item: coffee 4 22 USD
    Then the client receives receipt containing item: milk 5 4.9 USD
    Then the client receives receipt containing item: milk 1 1.05 USD
    Then the client receives receipt containing item: water 1 0.5 USD