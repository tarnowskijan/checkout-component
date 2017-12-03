package org.shopping.domain.checkout;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.shopping.domain.cart.CartItem;
import org.shopping.domain.cart.IShoppingCartService;
import org.shopping.domain.cart.Product;
import org.shopping.domain.cart.ProductFactory;

import java.math.BigDecimal;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CheckoutServiceTest {

    private static final String CART_ID = "cartId";
    private static final Product MILK = new ProductFactory().create("milk", "Milk");
    private static final Product SUGAR = new ProductFactory().create("sugar", "Sugar");
    private static final Product COFFEE = new ProductFactory().create("coffee", "Coffee");
    private static final Money ONE_USD = Money.of(BigDecimal.ONE, "USD");
    private static final Money TEN_USD = Money.of(BigDecimal.TEN, "USD");

    private CheckoutService checkoutService;

    @Before
    public void setUp() {
        IShoppingCartService shoppingCartService = mock(IShoppingCartService.class);
        given(shoppingCartService.getItems(eq(CART_ID))).willReturn(asList(
                CartItem.create(MILK, 4),
                CartItem.create(SUGAR, 10),
                CartItem.create(COFFEE, 15)
        ));

        IProductPricingRepository productPricingRepository = mock(IProductPricingRepository.class);
        given(productPricingRepository.findByProduct(MILK)).willReturn(new ProductPricing(ONE_USD));
        given(productPricingRepository.findByProduct(SUGAR)).willReturn(new ProductPricing(TEN_USD));
        given(productPricingRepository.findByProduct(COFFEE)).willReturn(new ProductPricing(TEN_USD));

        ISingleProductDiscountPolicyRepository singleProductDiscountPolicyRepository = mock(ISingleProductDiscountPolicyRepository.class);
        given(singleProductDiscountPolicyRepository.findAll()).willReturn(asList(
                new SpecialPriceSingleProductDiscountPolicy(MILK.getId(), 2, ONE_USD),
                new SpecialPriceSingleProductDiscountPolicy(COFFEE.getId(), 10, TEN_USD.multiply(8))
        ));

        DiscountEvaluationService discountEvaluationService = new DiscountEvaluationService(singleProductDiscountPolicyRepository);
        checkoutService = new CheckoutService(shoppingCartService, productPricingRepository, discountEvaluationService);
    }

    @Test
    public void shouldEvaluateUsingStandardPriceWhenCartItemHasNoApplicableDiscount() {
        // WHEN
        Receipt receipt = checkoutService.checkout(CART_ID);

        // THEN
        assertThat(receipt.getItems()).filteredOn(item -> item.getProduct().equals(SUGAR))
                .extracting(ReceiptItem::getQuantity, ReceiptItem::getValue)
                .containsOnly(tuple(10, TEN_USD.multiply(10)));
    }

    @Test
    public void shouldEvaluateRemainingQuantityUsingStandardPriceWhenCartItemWasPartiallyEvaluatedByDiscount() {
        // WHEN
        Receipt receipt = checkoutService.checkout(CART_ID);

        // THEN
        assertThat(receipt.getItems()).filteredOn(item -> item.getProduct().equals(COFFEE))
                .extracting(ReceiptItem::getQuantity, ReceiptItem::getValue)
                .containsOnly(tuple(10, TEN_USD.multiply(8)), tuple(5, TEN_USD.multiply(5)));
    }

    @Test
    public void shouldNotEvaluateCartItemAgainWhenItWasFullyEvaluatedByApplicableDiscount() {
        // WHEN
        Receipt receipt = checkoutService.checkout(CART_ID);

        // THEN
        assertThat(receipt.getItems()).filteredOn(item -> item.getProduct().equals(MILK))
                .extracting(ReceiptItem::getQuantity, ReceiptItem::getValue)
                .containsOnly(tuple(4, ONE_USD.multiply(2)));
    }

    @Test
    public void shouldReturnReceiptWithDiscountedAndNonDiscountedItems() {
        // WHEN
        Receipt receipt = checkoutService.checkout(CART_ID);

        // THEN
        assertThat(receipt.getItems())
                .extracting(ReceiptItem::getProduct, ReceiptItem::getQuantity, ReceiptItem::getValue)
                .containsOnly(
                        tuple(MILK, 4, ONE_USD.multiply(2)),
                        tuple(SUGAR, 10, TEN_USD.multiply(10)),
                        tuple(COFFEE, 10, TEN_USD.multiply(8)),
                        tuple(COFFEE, 5, TEN_USD.multiply(5))
                );
    }
}