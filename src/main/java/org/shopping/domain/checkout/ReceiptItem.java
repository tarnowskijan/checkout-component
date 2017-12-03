package org.shopping.domain.checkout;

import org.shopping.domain.cart.Product;

import javax.money.MonetaryAmount;

public class ReceiptItem {

    private final Product product;
    private final int quantity;
    private final MonetaryAmount value;

    ReceiptItem(Product product, int quantity, MonetaryAmount value) {
        this.product = product;
        this.quantity = quantity;
        this.value = value;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public MonetaryAmount getValue() {
        return value;
    }
}
