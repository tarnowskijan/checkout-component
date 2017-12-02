package org.shopping.domain.cart;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CartItemTest {

    private Product product;

    @Before
    public void setUp() {
        product = new Product("id", "name");
    }

    @Test
    public void shouldCreateCartItemWithProductAndQuantity() {
        // GIVEN
        int expectedQuantity = 2;

        // WHEN
        CartItem item = CartItem.create(product, expectedQuantity);

        // THEN
        assertThat(item).isNotNull();
        assertThat(item.getProduct()).isEqualTo(product);
        assertThat(item.getQuantity()).isEqualTo(expectedQuantity);
        assertThat(item.isNotEmpty()).isTrue();
    }

    @Test
    public void shouldAdjustQuantityToZeroWhenNegativeNumberProvided() {
        // WHEN
        CartItem item = CartItem.create(product, -1);

        // THEN
        assertThat(item.getQuantity()).isEqualTo(0);
    }

    @Test
    public void shouldBeEmptyWhenQuantityIsEqualToZero() {
        // WHEN
        CartItem item = CartItem.create(product, 0);

        // THEN
        assertThat(item.isNotEmpty()).isFalse();
    }

    @Test
    public void shouldCreateCopyWithIncreasedQuantity() {
        // GIVEN
        CartItem originalItem = CartItem.create(product, 2);

        // WHEN
        CartItem item = originalItem.withIncreasedQuantity(3);

        // THEN
        assertThat(item.getQuantity()).isEqualTo(5);
        assertThat(originalItem.getQuantity()).isEqualTo(2);
    }

    @Test
    public void shouldCreateCopyWithDecreasedQuantity() {
        // GIVEN
        CartItem originalItem = CartItem.create(product, 3);

        // WHEN
        CartItem item = originalItem.withDecreasedQuantity(2);

        // THEN
        assertThat(item.getQuantity()).isEqualTo(1);
        assertThat(originalItem.getQuantity()).isEqualTo(3);
    }
}