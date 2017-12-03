package org.shopping.infrastructure.checkout;

import org.shopping.domain.cart.Product;
import org.shopping.domain.checkout.IProductPricingRepository;
import org.shopping.domain.checkout.ProductPricing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
class ProductPricingRepository implements IProductPricingRepository {

    private static final String SELECT_PRODUCT_PRICING_BY_PRODUCT_ID_QUERY = "select PRICE, CURRENCY from PRODUCT_PRICING where PRODUCT_ID = ?";

    private final JdbcTemplate jdbcTemplate;
    private final ProductPricingMapper productPricingMapper;

    @Autowired
    ProductPricingRepository(DataSource dataSource, ProductPricingMapper productPricingMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.productPricingMapper = productPricingMapper;
    }

    @Override
    public ProductPricing findByProduct(Product product) {
        return jdbcTemplate.queryForObject(SELECT_PRODUCT_PRICING_BY_PRODUCT_ID_QUERY, productPricingMapper, product.getId());
    }
}
