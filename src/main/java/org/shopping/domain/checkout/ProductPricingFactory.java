package org.shopping.domain.checkout;

import javax.money.MonetaryAmount;

import static java.util.Objects.requireNonNull;

public final class ProductPricingFactory {

    private ProductPricingFactory() {
    }

    public static ProductPricing create(MonetaryAmount price) {
        return new ProductPricing(requireNonNull(price));
    }
}
