package org.shopping.infrastructure.cart;

import org.junit.Before;
import org.junit.Test;
import org.shopping.domain.cart.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ProductMapperTest {

    private static final String ID_COLUMN_NAME = "ID";
    private static final String NAME_COLUMN_NAME = "NAME";

    private ProductMapper productMapper;

    @Before
    public void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    public void shouldMapResultSetToProduct() throws SQLException {
        // GIVEN
        String expectedId = "productId";
        String expectedName = "productName";
        ResultSet resultSet = mock(ResultSet.class);
        given(resultSet.getString(eq(ID_COLUMN_NAME))).willReturn(expectedId);
        given(resultSet.getString(eq(NAME_COLUMN_NAME))).willReturn(expectedName);

        // WHEN
        Product product = productMapper.mapRow(resultSet, 0);

        // THEN
        assertThat(product.getId()).isEqualTo(expectedId);
        assertThat(product.getName()).isEqualTo(expectedName);
    }
}