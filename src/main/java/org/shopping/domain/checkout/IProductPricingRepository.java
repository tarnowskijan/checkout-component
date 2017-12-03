package org.shopping.domain.checkout;

import org.shopping.domain.cart.Product;

public interface IProductPricingRepository {
    ProductPricing findByProduct(Product product);
}
