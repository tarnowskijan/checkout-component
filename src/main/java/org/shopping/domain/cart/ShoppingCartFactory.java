package org.shopping.domain.cart;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class ShoppingCartFactory {

    ShoppingCart create() {
        String cartId = UUID.randomUUID().toString();
        return new ShoppingCart(cartId);
    }
}
