package org.shopping.domain.checkout;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.shopping.domain.cart.CartItem;
import org.shopping.domain.cart.Product;
import org.shopping.domain.cart.ProductFactory;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DiscountEvaluationServiceTest {

    private static final Product MILK = ProductFactory.create("milk", "Milk");
    private static final Product SUGAR = ProductFactory.create("sugar", "Sugar");

    private DiscountEvaluationService discountEvaluationService;
    private ISingleProductDiscountPolicyRepository singleProductDiscountPolicyRepository;
    private Collection<CartItem> cartItems;

    @Before
    public void setUp() {
        singleProductDiscountPolicyRepository = mock(ISingleProductDiscountPolicyRepository.class);
        discountEvaluationService = new DiscountEvaluationService(singleProductDiscountPolicyRepository);
        cartItems = asList(CartItem.create(MILK, 5), CartItem.create(SUGAR, 10));
    }

    @Test
    public void shouldReturnNoItemWhenThereIsNoSingleProductDiscountPolicy() {
        // GIVEN
        given(singleProductDiscountPolicyRepository.findAll()).willReturn(emptyList());

        // WHEN
        List<ReceiptItem> receiptItems = discountEvaluationService.evaluate(cartItems);

        // THEN
        assertThat(receiptItems).isEmpty();
    }

    @Test
    public void shouldApplyOnlyFirstSingleProductDiscountPolicyApplicableForCartItem() {
        // GIVEN
        Money expectedValue = Money.of(BigDecimal.ONE, "USD");
        int expectedQuantity = 3;
        given(singleProductDiscountPolicyRepository.findAll()).willReturn(asList(
                new SpecialPriceSingleProductDiscountPolicy(MILK.getId(), expectedQuantity, expectedValue),
                new SpecialPriceSingleProductDiscountPolicy(MILK.getId(), 2, Money.of(BigDecimal.TEN, "USD"))
        ));

        // WHEN
        List<ReceiptItem> receiptItems = discountEvaluationService.evaluate(cartItems);

        // THEN
        assertThat(receiptItems).extracting(ReceiptItem::getQuantity, ReceiptItem::getValue)
                .containsOnly(tuple(expectedQuantity, expectedValue));
    }

    @Test
    public void shouldApplySingleProductDiscountPoliciesForAllApplicableCartItems() {
        // GIVEN
        int expectedMilkQuantity = 5;
        Money expectedMilkValue = Money.of(BigDecimal.ONE, "USD");
        int expectedSugarQuantity = 10;
        Money expectedSugarValue = Money.of(BigDecimal.TEN, "USD");
        given(singleProductDiscountPolicyRepository.findAll()).willReturn(asList(
                new SpecialPriceSingleProductDiscountPolicy(MILK.getId(), expectedMilkQuantity, expectedMilkValue),
                new SpecialPriceSingleProductDiscountPolicy(SUGAR.getId(), expectedSugarQuantity, expectedSugarValue)
        ));

        // WHEN
        List<ReceiptItem> receiptItems = discountEvaluationService.evaluate(cartItems);

        // THEN
        assertThat(receiptItems).extracting(ReceiptItem::getProduct, ReceiptItem::getQuantity, ReceiptItem::getValue)
                .containsOnly(tuple(MILK, expectedMilkQuantity, expectedMilkValue), tuple(SUGAR, expectedSugarQuantity, expectedSugarValue));
    }

    @Test
    public void shouldNotEvaluateCartItemsWithoutApplicableSingleProductDiscountPolicy() {
        // GIVEN
        given(singleProductDiscountPolicyRepository.findAll()).willReturn(singletonList(
                new SpecialPriceSingleProductDiscountPolicy("eggs", 1, Money.of(BigDecimal.ONE, "USD"))
        ));

        // WHEN
        List<ReceiptItem> receiptItems = discountEvaluationService.evaluate(cartItems);

        // THEN
        assertThat(receiptItems).isEmpty();
    }
}