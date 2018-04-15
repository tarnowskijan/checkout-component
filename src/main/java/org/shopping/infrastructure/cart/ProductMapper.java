package org.shopping.infrastructure.cart;

import org.shopping.domain.cart.Product;
import org.shopping.domain.cart.ProductFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
class ProductMapper implements RowMapper<Product> {

    private static final String ID_COLUMN = "ID";
    private static final String NAME_COLUMN = "NAME";

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        String productId = rs.getString(ID_COLUMN);
        String productName = rs.getString(NAME_COLUMN);
        return ProductFactory.create(productId, productName);
    }
}
