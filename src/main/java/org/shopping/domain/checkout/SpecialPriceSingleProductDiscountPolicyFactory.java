package org.shopping.domain.checkout;

import org.springframework.stereotype.Component;

import javax.money.MonetaryAmount;

import static java.util.Objects.requireNonNull;

@Component
public class SpecialPriceSingleProductDiscountPolicyFactory {

    public SpecialPriceSingleProductDiscountPolicy create(String applicableProductId, int applicableQuantity, MonetaryAmount specialPrice) {
        return new SpecialPriceSingleProductDiscountPolicy(requireNonNull(applicableProductId), applicableQuantity, requireNonNull(specialPrice));
    }
}
