package org.shopping.domain.checkout;

import org.shopping.domain.cart.ShoppingCart;

public interface ICheckoutService {
    Receipt checkout(ShoppingCart shoppingCart);
}
