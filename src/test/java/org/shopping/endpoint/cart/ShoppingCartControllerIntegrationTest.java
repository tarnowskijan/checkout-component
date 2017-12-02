package org.shopping.endpoint.cart;

import org.junit.Test;
import org.shopping.BaseIntegrationTest;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ShoppingCartControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    public void shouldCreateNewCart() throws Exception {
        // WHEN
        String cartId = createCart();

        // THEN
        assertThat(cartId).isNotEmpty();
        mockMvc.perform(get("/carts/{cartId}", cartId)).andExpect(status().isOk());
    }

    @Test
    public void shouldGetCartItems() throws Exception {
        // GIVEN
        String expectedProductId = "milk";
        int expectedQuantity = 2;
        String cartId = createCart();
        addItems(cartId, expectedProductId, expectedQuantity);

        // WHEN
        CartItemDto[] cartItems = getItems(cartId);

        // THEN
        assertThat(cartItems).hasSize(1).extracting(CartItemDto::getProductId, CartItemDto::getQuantity)
                .containsExactly(tuple(expectedProductId, expectedQuantity));
    }

    @Test
    public void shouldAddItemsToExistingCart() throws Exception {
        // GIVEN
        String cartId = createCart();

        // WHEN
        addItems(cartId, "milk", 2);
        addItems(cartId, "sugar", 5);

        // THEN
        CartItemDto[] cartItems = getItems(cartId);
        assertThat(cartItems).hasSize(2).extracting(CartItemDto::getProductId, CartItemDto::getQuantity)
                .containsExactlyInAnyOrder(tuple("milk", 2), tuple("sugar", 5));
    }

    @Test
    public void shouldRemoveItemsFromExistingCart() throws Exception {
        // GIVEN
        String cartId = createCart();
        addItems(cartId, "sugar", 5);

        // WHEN
        mockMvc.perform(delete("/carts/{cartId}/items/{product}/{qty}", cartId, "sugar", 2))
                .andExpect(status().isOk());

        // THEN
        CartItemDto[] cartItems = getItems(cartId);
        assertThat(cartItems).hasSize(1).extracting(CartItemDto::getProductId, CartItemDto::getQuantity)
                .containsExactlyInAnyOrder(tuple("sugar", 3));
    }

    private void addItems(String cartId, String productId, int quantity) throws Exception {
        mockMvc.perform(post("/carts/{cartId}/items/{product}/{qty}", cartId, productId, quantity))
                .andExpect(status().isOk());
    }

    private String createCart() throws Exception {
        MvcResult createCartResult = mockMvc.perform(post("/carts")).andReturn();
        return createCartResult.getResponse().getContentAsString();
    }

    private CartItemDto[] getItems(String cartId) throws Exception {
        MvcResult result = mockMvc.perform(get("/carts/{cartId}", cartId))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), CartItemDto[].class);
    }
}
