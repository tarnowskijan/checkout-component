Feature: Shopping Cart

  Scenario: Client creates new shopping cart
    When the client calls /shopping/carts using POST method
    Then the client receives status code of 201
    Then the client receives ID of created cart in response body

  Scenario: Client adds items to shopping cart
    Given the client has shopping cart ID
    When the client calls /shopping/carts/{cartId}/items/milk/5 using POST method
    Then the client receives status code of 200
    When the client calls /shopping/carts/{cartId} using GET method
    Then the client receives list of items containing: milk 5

  Scenario: Client removes items from shopping cart
    Given the client has shopping cart ID
    Given the client has - 4 coffee - in shopping cart
    When the client calls /shopping/carts/{cartId}/items/coffee/3 using DELETE method
    Then the client receives status code of 200
    When the client calls /shopping/carts/{cartId} using GET method
    Then the client receives list of items containing: coffee 1

  Scenario: Client retrieves items from shopping cart
    Given the client has shopping cart ID
    Given the client has - 4 coffee - in shopping cart
    When the client calls /shopping/carts/{cartId} using GET method
    Then the client receives status code of 200
    Then the client receives list of items containing: coffee 4