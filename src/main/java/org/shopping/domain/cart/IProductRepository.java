package org.shopping.domain.cart;

public interface IProductRepository {
    Product findById(String productId) throws ProductNotFoundException;
}
