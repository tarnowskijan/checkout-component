package org.shopping.endpoint.checkout;

import org.junit.Test;
import org.shopping.BaseIntegrationTest;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CheckoutControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    public void shouldCheckoutShoppingCart() throws Exception {
        // GIVEN
        String cartId = createCart();
        addItems(cartId, "milk", 12);
        addItems(cartId, "eggs", 5);
        addItems(cartId, "coffee", 100);

        // WHEN
        MvcResult result = mockMvc.perform(get("/checkouts/{cartId}", cartId))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        ReceiptDto receipt = objectMapper.readValue(result.getResponse().getContentAsString(), ReceiptDto.class);
        assertThat(receipt.getItems())
                .extracting(ReceiptItemDto::getProductId, ReceiptItemDto::getQuantity, ReceiptItemDto::getValue, ReceiptItemDto::getCurrency)
                .containsOnly(
                        tuple("milk", 10, "9.8", "USD"),
                        tuple("milk", 2, "2.1", "USD"),
                        tuple("eggs", 5, "15", "USD"),
                        tuple("coffee", 100, "480", "USD")
                );
    }

    private String createCart() throws Exception {
        MvcResult createCartResult = mockMvc.perform(post("/carts")).andExpect(status().is2xxSuccessful()).andReturn();
        return createCartResult.getResponse().getContentAsString();
    }

    private void addItems(String cartId, String productId, int quantity) throws Exception {
        mockMvc.perform(post("/carts/{cartId}/items/{product}/{qty}", cartId, productId, quantity))
                .andExpect(status().isOk());
    }
}