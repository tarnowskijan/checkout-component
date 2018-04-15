package org.shopping.endpoint.cart;

import org.junit.Before;
import org.junit.Test;
import org.shopping.domain.cart.*;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ShoppingCartApplicationServiceTest {

    private static final Product PRODUCT = ProductFactory.create("productId", "name");
    private static final String MISSING_CART_ID = "missing";
    private static final String MISSING_PRODUCT_ID = "missing_product";

    private ShoppingCartApplicationService shoppingCartApplicationService;
    private IShoppingCartRepository shoppingCartRepository;
    private ShoppingCart shoppingCart;

    @Before
    public void setUp() {
        shoppingCartRepository = mock(IShoppingCartRepository.class);
        IProductRepository productRepository = mock(IProductRepository.class);
        shoppingCartApplicationService = new ShoppingCartApplicationService(shoppingCartRepository, productRepository);

        shoppingCart = ShoppingCartFactory.create();
        given(shoppingCartRepository.findById(eq(shoppingCart.getId()))).willReturn(shoppingCart);
        given(shoppingCartRepository.findById(eq(MISSING_CART_ID))).willThrow(new ShoppingCartNotFoundException());
        given(productRepository.findById(eq(PRODUCT.getId()))).willReturn(PRODUCT);
        given(productRepository.findById(eq(MISSING_PRODUCT_ID))).willThrow(new ProductNotFoundException());
    }

    @Test
    public void shouldCreateEmptyCart() {
        // WHEN
        shoppingCartApplicationService.createCart();

        // THEN
        verify(shoppingCartRepository).save(argThat(cart -> {
            assertThat(cart).isNotNull();
            assertThat(cart.getItems()).isEmpty();
            return true;
        }));
    }

    @Test
    public void shouldReturnIdOfCreatedCart() {
        // WHEN
        String id = shoppingCartApplicationService.createCart();

        // THEN
        verify(shoppingCartRepository).save(argThat(cart -> {
            assertThat(cart.getId()).isEqualTo(id);
            return true;
        }));
    }

    @Test
    public void shouldAddItemsToCart() {
        // WHEN
        int quantity = 1;
        shoppingCartApplicationService.addItem(shoppingCart.getId(), PRODUCT.getId(), quantity);

        // THEN
        assertThat(shoppingCart.getItems()).hasSize(1)
                .first().satisfies(item -> {
            assertThat(item.getProduct()).isEqualTo(PRODUCT);
            assertThat(item.getQuantity()).isEqualTo(quantity);
        });
    }

    @Test
    public void shouldRemoveItemsFromCart() {
        // WHEN
        int quantity = 2;
        int remainingQuantity = 1;
        shoppingCartApplicationService.addItem(shoppingCart.getId(), PRODUCT.getId(), quantity);
        shoppingCartApplicationService.removeItem(shoppingCart.getId(), PRODUCT.getId(), remainingQuantity);

        // THEN
        assertThat(shoppingCart.getItems()).hasSize(1)
                .first().satisfies(item -> {
            assertThat(item.getProduct()).isEqualTo(PRODUCT);
            assertThat(item.getQuantity()).isEqualTo(remainingQuantity);
        });
    }

    @Test
    public void shouldGetItemsFromCart() {
        // GIVEN
        shoppingCartApplicationService.addItem(shoppingCart.getId(), PRODUCT.getId(), 1);

        // WHEN
        Collection<CartItem> items = shoppingCartApplicationService.getItems(shoppingCart.getId());

        // THEN
        assertThat(items).hasSize(1);
    }

    @Test
    public void shouldThrowExceptionWhenAddingToMissingCart() {
        // WHEN - THEN
        assertThatThrownBy(() -> shoppingCartApplicationService.addItem(MISSING_CART_ID, PRODUCT.getId(), 1))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }

    @Test
    public void shouldThrowExceptionWhenRemovingFromMissingCart() {
        // WHEN - THEN
        assertThatThrownBy(() -> shoppingCartApplicationService.removeItem(MISSING_CART_ID, PRODUCT.getId(), 1))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }

    @Test
    public void shouldThrowExceptionWhenAddingMissingProduct() {
        // WHEN - THEN
        assertThatThrownBy(() -> shoppingCartApplicationService.addItem(shoppingCart.getId(), MISSING_PRODUCT_ID, 1))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void shouldThrowExceptionWhenRemovingMissingProduct() {
        // WHEN - THEN
        assertThatThrownBy(() -> shoppingCartApplicationService.removeItem(shoppingCart.getId(), MISSING_PRODUCT_ID, 1))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void shouldThrowExceptionWhenGettingItemsFromMissingCart() {
        // GIVEN
        String missingCartId = "missing";

        // WHEN - THEN
        assertThatThrownBy(() -> shoppingCartApplicationService.getItems(missingCartId))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }
}