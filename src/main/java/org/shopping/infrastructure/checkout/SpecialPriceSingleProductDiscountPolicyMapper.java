package org.shopping.infrastructure.checkout;

import org.javamoney.moneta.Money;
import org.shopping.domain.checkout.SingleProductDiscountPolicy;
import org.shopping.domain.checkout.SpecialPriceSingleProductDiscountPolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
class SpecialPriceSingleProductDiscountPolicyMapper implements RowMapper<SingleProductDiscountPolicy> {

    private static final String PRODUCT_ID_COLUMN_NAME = "PRODUCT_ID";
    private static final String APPLICABLE_QUANTITY_COLUMN_NAME = "APPLICABLE_QUANTITY";
    private static final String PRICE_COLUMN_NAME = "PRICE";
    private static final String CURRENCY_COLUMN_NAME = "CURRENCY";

    private final SpecialPriceSingleProductDiscountPolicyFactory specialPriceSingleProductDiscountPolicyFactory;

    @Autowired
    public SpecialPriceSingleProductDiscountPolicyMapper(SpecialPriceSingleProductDiscountPolicyFactory specialPriceSingleProductDiscountPolicyFactory) {
        this.specialPriceSingleProductDiscountPolicyFactory = specialPriceSingleProductDiscountPolicyFactory;
    }

    @Override
    public SingleProductDiscountPolicy mapRow(ResultSet rs, int rowNum) throws SQLException {
        String productId = rs.getString(PRODUCT_ID_COLUMN_NAME);
        int applicableQuantity = rs.getInt(APPLICABLE_QUANTITY_COLUMN_NAME);
        BigDecimal price = rs.getBigDecimal(PRICE_COLUMN_NAME);
        String currency = rs.getString(CURRENCY_COLUMN_NAME);
        return specialPriceSingleProductDiscountPolicyFactory.create(productId, applicableQuantity, Money.of(price, currency));
    }
}
