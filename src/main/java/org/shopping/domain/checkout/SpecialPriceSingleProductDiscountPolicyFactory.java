package org.shopping.domain.checkout;

import javax.money.MonetaryAmount;

import static java.util.Objects.requireNonNull;

public final class SpecialPriceSingleProductDiscountPolicyFactory {

    private SpecialPriceSingleProductDiscountPolicyFactory() {
    }

    public static SpecialPriceSingleProductDiscountPolicy create(String applicableProductId, int applicableQuantity,
                                                                 MonetaryAmount specialPrice) {
        return new SpecialPriceSingleProductDiscountPolicy(requireNonNull(applicableProductId), applicableQuantity,
                requireNonNull(specialPrice));
    }
}
