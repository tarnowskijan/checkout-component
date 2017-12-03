package org.shopping.domain.checkout;

import org.springframework.stereotype.Component;

import javax.money.MonetaryAmount;

import static java.util.Objects.requireNonNull;

@Component
public class ProductPricingFactory {

    public ProductPricing create(MonetaryAmount price) {
        return new ProductPricing(requireNonNull(price));
    }
}
