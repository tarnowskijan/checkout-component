package org.shopping.domain.checkout;

import org.shopping.domain.cart.CartItem;

public interface SingleProductDiscountPolicy {
    boolean isApplicable(CartItem cartItem);

    ReceiptItem evaluate(CartItem cartItem);
}
