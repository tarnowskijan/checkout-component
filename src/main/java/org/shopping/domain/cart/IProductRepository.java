package org.shopping.domain.cart;

import java.util.Optional;

public interface IProductRepository {
    Optional<Product> findById(String productId);
}
