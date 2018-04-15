package org.shopping.domain.cart;

public interface IShoppingCartRepository {
    ShoppingCart findById(String cartId) throws ShoppingCartNotFoundException;

    void save(ShoppingCart cart);
}
