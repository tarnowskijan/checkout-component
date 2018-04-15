package org.shopping.domain.cart;

import java.util.UUID;

public final class ShoppingCartFactory {

    private ShoppingCartFactory() {
    }

    public static ShoppingCart create() {
        String cartId = UUID.randomUUID().toString();
        return new ShoppingCart(cartId);
    }
}
