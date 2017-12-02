package org.shopping.domain.cart;

import java.util.Optional;

public interface IShoppingCartRepository {
    Optional<ShoppingCart> findById(String cartId);

    void save(ShoppingCart cart);
}
