package org.shopping.domain.checkout;

import org.shopping.domain.cart.CartItem;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receipt {

    private final List<ReceiptItem> items;

    Receipt() {
        this.items = new ArrayList<>();
    }

    public List<ReceiptItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    void registerItem(CartItem cartItem, ProductPricing pricing) {
        MonetaryAmount value = pricing.getPrice().multiply(cartItem.getQuantity());
        ReceiptItem item = new ReceiptItem(cartItem.getProduct(), cartItem.getQuantity(), value);
        items.add(item);
    }
}
