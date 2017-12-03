package org.shopping.infrastructure.checkout;

import org.shopping.domain.checkout.ISingleProductDiscountPolicyRepository;
import org.shopping.domain.checkout.SingleProductDiscountPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
class SingleProductDiscountPolicyRepository implements ISingleProductDiscountPolicyRepository {

    private static final String SELECT_SPECIAL_PRICE_SINGLE_PRODUCT_DISCOUNTS_QUERY = "select PRODUCT_ID, APPLICABLE_QUANTITY, PRICE, CURRENCY from SPECIAL_PRICE_SINGLE_PRODUCT_DISCOUNT";

    private final JdbcTemplate jdbcTemplate;
    private final SpecialPriceSingleProductDiscountPolicyMapper specialPriceSingleProductDiscountPolicyMapper;

    @Autowired
    SingleProductDiscountPolicyRepository(DataSource dataSource, SpecialPriceSingleProductDiscountPolicyMapper specialPriceSingleProductDiscountPolicyMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.specialPriceSingleProductDiscountPolicyMapper = specialPriceSingleProductDiscountPolicyMapper;
    }

    @Override
    public List<SingleProductDiscountPolicy> findAll() {
        return jdbcTemplate.query(SELECT_SPECIAL_PRICE_SINGLE_PRODUCT_DISCOUNTS_QUERY, specialPriceSingleProductDiscountPolicyMapper);
    }
}
