package org.shopping.infrastructure.checkout;

import org.javamoney.moneta.Money;
import org.shopping.domain.checkout.ProductPricing;
import org.shopping.domain.checkout.ProductPricingFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
class ProductPricingMapper implements RowMapper<ProductPricing> {

    private static final String PRICE_COLUMN_NAME = "PRICE";
    private static final String CURRENCY_COLUMN_NAME = "CURRENCY";

    @Override
    public ProductPricing mapRow(ResultSet rs, int rowNum) throws SQLException {
        BigDecimal price = rs.getBigDecimal(PRICE_COLUMN_NAME);
        String currency = rs.getString(CURRENCY_COLUMN_NAME);
        return ProductPricingFactory.create(Money.of(price, currency));
    }
}
