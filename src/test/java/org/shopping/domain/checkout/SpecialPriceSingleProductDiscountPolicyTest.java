package org.shopping.domain.checkout;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.shopping.domain.cart.CartItem;
import org.shopping.domain.cart.Product;
import org.shopping.domain.cart.ProductFactory;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecialPriceSingleProductDiscountPolicyTest {

    private static final String APPLICABLE_PRODUCT_ID = "productId";
    private static final int APPLICABLE_QUANTITY = 10;
    private static final Money SPECIAL_PRICE = Money.of(BigDecimal.ONE, "USD");

    private SpecialPriceSingleProductDiscountPolicy specialPriceSingleProductDiscountPolicy;
    private Product applicableProduct;
    private final ProductFactory productFactory = new ProductFactory();

    @Before
    public void setUp() {
        specialPriceSingleProductDiscountPolicy = new SpecialPriceSingleProductDiscountPolicy(APPLICABLE_PRODUCT_ID, APPLICABLE_QUANTITY, SPECIAL_PRICE);
        applicableProduct = productFactory.create(APPLICABLE_PRODUCT_ID, "");
    }

    @Test
    public void shouldBeApplicableForCartItemWithMatchingProductIdAndQuantity() {
        // GIVEN
        CartItem cartItem = CartItem.create(applicableProduct, APPLICABLE_QUANTITY);

        // WHEN
        boolean applicable = specialPriceSingleProductDiscountPolicy.isApplicable(cartItem);

        // THEN
        assertThat(applicable).isTrue();
    }

    @Test
    public void shouldBeApplicableForCartItemWithMatchingProductIdAndGreaterQuantity() {
        // GIVEN
        CartItem cartItem = CartItem.create(applicableProduct, APPLICABLE_QUANTITY + 20);

        // WHEN
        boolean applicable = specialPriceSingleProductDiscountPolicy.isApplicable(cartItem);

        // THEN
        assertThat(applicable).isTrue();
    }

    @Test
    public void shouldNotBeApplicableForCartItemWithMatchingProductIdAndLowerQuantity() {
        // GIVEN
        CartItem cartItem = CartItem.create(applicableProduct, APPLICABLE_QUANTITY - 1);

        // WHEN
        boolean applicable = specialPriceSingleProductDiscountPolicy.isApplicable(cartItem);

        // THEN
        assertThat(applicable).isFalse();
    }

    @Test
    public void shouldNotBeApplicableForCartItemWithoutMatchingProductId() {
        // GIVEN
        Product product = productFactory.create("not_applicable", "");
        CartItem cartItem = CartItem.create(product, APPLICABLE_QUANTITY);

        // WHEN
        boolean applicable = specialPriceSingleProductDiscountPolicy.isApplicable(cartItem);

        // THEN
        assertThat(applicable).isFalse();
    }

    @Test
    public void shouldSetProductInEvaluatedReceiptItem() {
        // GIVEN
        CartItem cartItem = CartItem.create(applicableProduct, APPLICABLE_QUANTITY);

        // WHEN
        ReceiptItem receiptItem = specialPriceSingleProductDiscountPolicy.evaluate(cartItem);

        // THEN
        assertThat(receiptItem.getProduct()).isEqualTo(applicableProduct);
    }

    @Test
    public void shouldEvaluateDiscountedReceiptItemWithQuantityEqualToApplicable() {
        // GIVEN
        CartItem cartItem = CartItem.create(applicableProduct, APPLICABLE_QUANTITY);

        // WHEN
        ReceiptItem receiptItem = specialPriceSingleProductDiscountPolicy.evaluate(cartItem);

        // THEN
        assertThat(receiptItem.getValue()).isEqualTo(SPECIAL_PRICE);
        assertThat(receiptItem.getQuantity()).isEqualTo(APPLICABLE_QUANTITY);
    }

    @Test
    public void shouldEvaluateDiscountedReceiptItemWithQuantityMoreThanApplicable() {
        // GIVEN
        CartItem cartItem = CartItem.create(applicableProduct, APPLICABLE_QUANTITY + (APPLICABLE_QUANTITY / 2));

        // WHEN
        ReceiptItem receiptItem = specialPriceSingleProductDiscountPolicy.evaluate(cartItem);

        // THEN
        assertThat(receiptItem.getValue()).isEqualTo(SPECIAL_PRICE);
        assertThat(receiptItem.getQuantity()).isEqualTo(APPLICABLE_QUANTITY);
    }

    @Test
    public void shouldEvaluateDiscountedReceiptItemWithMultipliedApplicableQuantity() {
        // GIVEN
        int expectedPacketsNumber = 3;
        CartItem cartItem = CartItem.create(applicableProduct, APPLICABLE_QUANTITY * expectedPacketsNumber + 1);

        // WHEN
        ReceiptItem receiptItem = specialPriceSingleProductDiscountPolicy.evaluate(cartItem);

        // THEN
        assertThat(receiptItem.getValue()).isEqualTo(SPECIAL_PRICE.multiply(expectedPacketsNumber));
        assertThat(receiptItem.getQuantity()).isEqualTo(APPLICABLE_QUANTITY * expectedPacketsNumber);
    }
}