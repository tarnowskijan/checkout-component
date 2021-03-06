package org.shopping.domain.checkout;

import javax.money.MonetaryAmount;

public class ProductPricing {

    private final MonetaryAmount price;

    ProductPricing(MonetaryAmount price) {
        this.price = price;
    }

    public MonetaryAmount getPrice() {
        return price;
    }
}
