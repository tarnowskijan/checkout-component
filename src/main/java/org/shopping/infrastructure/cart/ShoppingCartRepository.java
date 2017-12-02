package org.shopping.infrastructure.cart;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.shopping.domain.cart.IShoppingCartRepository;
import org.shopping.domain.cart.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
class ShoppingCartRepository implements IShoppingCartRepository {

    private final Cache<String, ShoppingCart> carts;

    @Autowired
    ShoppingCartRepository(@Value("${shoppingCart.cache.expireAfterAccessMinutes:30}") long expireAfterAccessMinutes,
                           @Value("${shoppingCart.cache.maximumSize:10000}") long maximumSize) {
        this.carts = CacheBuilder.newBuilder()
                .expireAfterAccess(expireAfterAccessMinutes, TimeUnit.MINUTES)
                .maximumSize(maximumSize)
                .build();
    }

    @Override
    public Optional<ShoppingCart> findById(String cartId) {
        return Optional.ofNullable(carts.getIfPresent(cartId));
    }

    @Override
    public void save(ShoppingCart cart) {
        carts.put(cart.getId(), cart);
    }
}
