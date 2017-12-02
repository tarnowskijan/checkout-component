package org.shopping.endpoint.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.shopping.domain.cart.CartItem;

class CartItemDto {
    private final String productId;
    private final int quantity;

    CartItemDto(CartItem item) {
        this(item.getProduct().getId(), item.getQuantity());
    }

    private CartItemDto(@JsonProperty("productId") String productId, @JsonProperty("quantity") int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    String getProductId() {
        return productId;
    }

    int getQuantity() {
        return quantity;
    }
}
