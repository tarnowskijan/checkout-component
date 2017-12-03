package org.shopping.domain.checkout;

public interface ICheckoutService {
    Receipt checkout(String shoppingCartId);
}
