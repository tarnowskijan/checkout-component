package org.shopping.domain.checkout;

import org.shopping.domain.cart.CartItem;

import javax.money.MonetaryAmount;

public class SpecialPriceSingleProductDiscountPolicy implements SingleProductDiscountPolicy {

    private final String applicableProductId;
    private final int applicableQuantity;
    private final MonetaryAmount specialPrice;

    SpecialPriceSingleProductDiscountPolicy(String applicableProductId, int applicableQuantity, MonetaryAmount specialPrice) {
        this.applicableProductId = applicableProductId;
        this.applicableQuantity = applicableQuantity;
        this.specialPrice = specialPrice;
    }

    @Override
    public boolean isApplicable(CartItem cartItem) {
        return applicableProductId.equals(cartItem.getProduct().getId())
                && cartItem.getQuantity() >= applicableQuantity;
    }

    @Override
    public ReceiptItem evaluate(CartItem cartItem) {
        int numberOfPackets = cartItem.getQuantity() / applicableQuantity;
        int discountedQuantity = numberOfPackets * applicableQuantity;
        MonetaryAmount value = specialPrice.multiply(numberOfPackets);
        return new ReceiptItem(cartItem.getProduct(), discountedQuantity, value);
    }
}
