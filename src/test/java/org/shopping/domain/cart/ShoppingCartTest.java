package org.shopping.domain.cart;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ShoppingCartTest {

    private static final Product MILK = new Product("milk", "Milk");
    private static final Product SUGAR = new Product("sugar", "Sugar");

    private ShoppingCart shoppingCart;

    @Before
    public void setUp() {
        shoppingCart = new ShoppingCart("id");
    }

    @Test
    public void shouldAddNewItems() {
        // WHEN
        shoppingCart.addItem(MILK, 3);
        shoppingCart.addItem(SUGAR, 5);

        // THEN
        assertThat(shoppingCart.getItems()).hasSize(2)
                .extracting(CartItem::getProduct, CartItem::getQuantity)
                .containsExactlyInAnyOrder(tuple(MILK, 3), tuple(SUGAR, 5));
    }

    @Test
    public void shouldUpdateQuantityOfExistingItem() {
        // GIVEN
        shoppingCart.addItem(MILK, 1);

        // WHEN
        shoppingCart.addItem(MILK, 3);

        // THEN
        assertThat(shoppingCart.getItems()).hasSize(1)
                .first().satisfies(item -> assertThat(item.getQuantity()).isEqualTo(4));
    }

    @Test
    public void shouldDecreaseQuantityOfExistingItem() {
        // GIVEN
        shoppingCart.addItem(MILK, 3);

        // WHEN
        shoppingCart.removeItem(MILK, 1);

        // THEN
        assertThat(shoppingCart.getItems()).hasSize(1)
                .first().satisfies(item -> assertThat(item.getQuantity()).isEqualTo(2));
    }

    @Test
    public void shouldRemoveItemWhenQuantityIsEqualToZero() {
        // GIVEN
        shoppingCart.addItem(MILK, 3);

        // WHEN
        shoppingCart.removeItem(MILK, 3);

        // THEN
        assertThat(shoppingCart.getItems()).isEmpty();
    }

    @Test
    public void shouldDoNothingWhenTryingToRemoveMissingProduct() {
        // GIVEN
        shoppingCart.addItem(MILK, 3);

        // WHEN
        shoppingCart.removeItem(new Product("missing", ""), 3);

        // THEN
        assertThat(shoppingCart.getItems()).hasSize(1);
    }
}