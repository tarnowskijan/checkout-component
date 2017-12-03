package org.shopping.endpoint.cart;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.shopping.AcceptanceTestConfiguration;
import org.shopping.AcceptanceTestState;
import org.shopping.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ContextConfiguration
@SpringBootTest(classes = {Application.class, AcceptanceTestConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ShoppingCartStepDefinitions {

    private static final String SERVER_URL = "http://127.0.0.1:8080";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AcceptanceTestState state;

    @When("^the client calls /shopping/carts using POST method$")
    public void whenTheClientCallsCartsUsingPostMethod() {
        ResponseEntity<String> latestResponse = restTemplate.postForEntity(SERVER_URL + "/shopping/carts", null, String.class);
        state.setLatestResponse(latestResponse);
    }

    @Then("^the client receives status code of (\\d+)$")
    public void thenTheClientReceivesStatusCode(int statusCode) {
        ResponseEntity<?> latestResponse = state.getLatestResponse();
        requireNonNull(latestResponse);
        assertThat(latestResponse.getStatusCodeValue()).isEqualTo(statusCode);
    }

    @Then("^the client receives ID of created cart in response body$")
    public void thenTheClientReceivesIdOfCreatedCartInResponseBody() {
        ResponseEntity<?> latestResponse = state.getLatestResponse();
        requireNonNull(latestResponse);
        assertThat(latestResponse.getBody()).isInstanceOfSatisfying(String.class,
                cartId -> assertThat(cartId).isNotEmpty());
    }

    @Given("^the client has shopping cart ID$")
    public void givenTheClientHasShoppingCartId() {
        String shoppingCartId = restTemplate.postForObject(SERVER_URL + "/shopping/carts", null, String.class);
        state.setLatestShoppingCartId(shoppingCartId);
    }

    @When("^the client calls /shopping/carts/\\{cartId\\}/items/(.+)/(.+) using (.+) method$")
    public void whenTheClientCallsItemsUsingPostMethod(String productId, int quantity, String method) {
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        String url = String.format("/shopping/carts/%s/items/%s/%s", state.getLatestShoppingCartId(), productId, quantity);
        ResponseEntity<Void> response = restTemplate.exchange(SERVER_URL + url, httpMethod, null, Void.class);
        state.setLatestResponse(response);
    }

    @Given("^the client has - (.+) (.+) - in shopping cart$")
    public void givenTheClientHas4CoffeesInShoppingCart(int quantity, String productId) {
        String url = String.format("/shopping/carts/%s/items/%s/%s", state.getLatestShoppingCartId(), productId, quantity);
        ResponseEntity<Void> response = restTemplate.exchange(SERVER_URL + url, HttpMethod.POST, null, Void.class);
        state.setLatestResponse(response);
    }

    @When("^the client calls /shopping/carts/\\{cartId\\} using GET method$")
    public void whenTheClientCallsCartsUsingGetMethod() {
        String url = String.format("/shopping/carts/%s", state.getLatestShoppingCartId());
        ResponseEntity<CartItemDto[]> response = restTemplate.getForEntity(SERVER_URL + url, CartItemDto[].class);
        state.setLatestResponse(response);
    }

    @Then("^the client receives list of items containing: (.+) (.+)$")
    public void thenTheClientReceivesListOfItemsContaining(String productId, int quantity) {
        CartItemDto[] cartItems = (CartItemDto[]) state.getLatestResponse().getBody();
        assertThat(cartItems).extracting(CartItemDto::getProductId, CartItemDto::getQuantity)
                .contains(tuple(productId, quantity));
    }
}
