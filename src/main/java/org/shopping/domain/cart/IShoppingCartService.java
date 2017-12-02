package org.shopping.domain.cart;

import java.util.Collection;

public interface IShoppingCartService {
    String createCart();

    void addItem(String cartId, String productId, int quantity) throws ShoppingCartNotFoundException, ProductNotFoundException;

    void removeItem(String cartId, String productId, int quantity) throws ShoppingCartNotFoundException, ProductNotFoundException;

    Collection<CartItem> getItems(String cartId);
}
