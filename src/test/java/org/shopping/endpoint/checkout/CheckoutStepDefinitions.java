package org.shopping.endpoint.checkout;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.shopping.AcceptanceTestConfiguration;
import org.shopping.AcceptanceTestState;
import org.shopping.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ContextConfiguration
@SpringBootTest(classes = {Application.class, AcceptanceTestConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CheckoutStepDefinitions {

    private static final String SERVER_URL = "http://127.0.0.1:8080";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AcceptanceTestState state;

    @When("^the client calls /shopping/checkouts/\\{cartId\\} using GET method$")
    public void whenTheClientCallsCheckoutUsingGetMethod() {
        String url = String.format("/shopping/checkouts/%s", state.getLatestShoppingCartId());
        ResponseEntity<ReceiptDto> response = restTemplate.getForEntity(SERVER_URL + url, ReceiptDto.class);
        state.setLatestResponse(response);
    }

    @Then("^the client receives receipt containing item: (.+) (.+) (.+) (.+)$")
    public void thenTheClientReceivesReceiptContainingItem(String productId, int quantity, String value, String currency) {
        ResponseEntity<?> latestResponse = state.getLatestResponse();
        assertThat(latestResponse.getBody()).isInstanceOfSatisfying(ReceiptDto.class, receipt ->
                assertThat(receipt.getItems())
                        .extracting(ReceiptItemDto::getProductId, ReceiptItemDto::getQuantity, ReceiptItemDto::getValue, ReceiptItemDto::getCurrency)
                        .contains(tuple(productId, quantity, value, currency)));
    }
}
