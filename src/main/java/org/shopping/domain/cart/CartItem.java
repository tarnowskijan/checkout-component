package org.shopping.domain.cart;

import static java.util.Objects.requireNonNull;

public class CartItem {

    private final Product product;
    private final int quantity;

    private CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    CartItem withIncreasedQuantity(int quantity) {
        return create(this.product, this.quantity + quantity);
    }

    public CartItem withDecreasedQuantity(int quantity) {
        return create(this.product, this.quantity - quantity);
    }

    public boolean isNotEmpty() {
        return quantity > 0;
    }

    static CartItem create(Product product, int quantity) {
        return new CartItem(requireNonNull(product), adjustQuantity(quantity));
    }

    private static int adjustQuantity(int quantity) {
        return quantity >= 0 ? quantity : 0;
    }
}
