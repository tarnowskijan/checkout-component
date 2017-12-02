package org.shopping.infrastructure.cart;

import org.shopping.domain.cart.IProductRepository;
import org.shopping.domain.cart.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
class ProductRepository implements IProductRepository {

    private static final String SELECT_PRODUCT_BY_ID_QUERY = "select ID, NAME from PRODUCT where ID = ?";

    private final JdbcTemplate jdbcTemplate;
    private final ProductMapper productMapper;

    @Autowired
    ProductRepository(DataSource dataSource, ProductMapper productMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.productMapper = productMapper;
    }

    @Override
    public Optional<Product> findById(String productId) {
        try {
            Product product = jdbcTemplate.queryForObject(SELECT_PRODUCT_BY_ID_QUERY, productMapper, productId);
            return Optional.of(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
