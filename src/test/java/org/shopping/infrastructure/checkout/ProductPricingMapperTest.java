package org.shopping.infrastructure.checkout;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.shopping.domain.checkout.ProductPricing;
import org.shopping.domain.checkout.ProductPricingFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ProductPricingMapperTest {

    private static final String PRICE_COLUMN_NAME = "PRICE";
    private static final String CURRENCY_COLUMN_NAME = "CURRENCY";

    private ProductPricingMapper productPricingMapper;

    @Before
    public void setUp() {
        productPricingMapper = new ProductPricingMapper(new ProductPricingFactory());
    }

    @Test
    public void shouldMapResultSetToProduct() throws SQLException {
        // GIVEN
        BigDecimal expectedPrice = BigDecimal.ONE;
        String expectedCurrency = "USD";
        ResultSet resultSet = mock(ResultSet.class);
        given(resultSet.getBigDecimal(eq(PRICE_COLUMN_NAME))).willReturn(expectedPrice);
        given(resultSet.getString(eq(CURRENCY_COLUMN_NAME))).willReturn(expectedCurrency);

        // WHEN
        ProductPricing productPricing = productPricingMapper.mapRow(resultSet, 0);

        // THEN
        assertThat(productPricing.getPrice()).isEqualTo(Money.of(expectedPrice, expectedCurrency));
    }
}