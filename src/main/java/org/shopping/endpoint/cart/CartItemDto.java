package org.shopping.endpoint.cart;

import org.shopping.domain.cart.CartItem;

class CartItemDto {
    private final String productId;
    private final int quantity;

    CartItemDto(CartItem item) {
        this.productId = item.getProduct().getId();
        this.quantity = item.getQuantity();
    }
}
