package org.shopping.domain.cart;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ShoppingCartServiceTest {

    private static final String CART_ID = "cartId";
    private static final Product PRODUCT = new Product("productId", "name");

    private ShoppingCartService shoppingCartService;
    private IShoppingCartRepository shoppingCartRepository;
    private ShoppingCart shoppingCart;

    @Before
    public void setUp() {
        shoppingCartRepository = mock(IShoppingCartRepository.class);
        IProductRepository productRepository = mock(IProductRepository.class);
        ShoppingCartFactory shoppingCartFactory = new ShoppingCartFactory();
        shoppingCartService = new ShoppingCartService(shoppingCartFactory, shoppingCartRepository, productRepository);

        shoppingCart = new ShoppingCart(CART_ID);
        given(shoppingCartRepository.findById(eq(CART_ID))).willReturn(Optional.of(shoppingCart));
        given(productRepository.findById(eq(PRODUCT.getId()))).willReturn(Optional.of(PRODUCT));
    }

    @Test
    public void shouldCreateEmptyCart() {
        // WHEN
        shoppingCartService.createCart();

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
        String id = shoppingCartService.createCart();

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
        shoppingCartService.addItem(CART_ID, PRODUCT.getId(), quantity);

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
        shoppingCartService.addItem(CART_ID, PRODUCT.getId(), quantity);
        shoppingCartService.removeItem(CART_ID, PRODUCT.getId(), remainingQuantity);

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
        shoppingCartService.addItem(CART_ID, PRODUCT.getId(), 1);

        // WHEN
        Collection<CartItem> items = shoppingCartService.getItems(CART_ID);

        // THEN
        assertThat(items).hasSize(1);
    }

    @Test
    public void shouldThrowExceptionWhenAddingToMissingCart() {
        // GIVEN
        String missingCartId = "missing";

        // WHEN - THEN
        assertThatThrownBy(() -> shoppingCartService.addItem(missingCartId, PRODUCT.getId(), 1))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }

    @Test
    public void shouldThrowExceptionWhenRemovingFromMissingCart() {
        // GIVEN
        String missingCartId = "missing";

        // WHEN - THEN
        assertThatThrownBy(() -> shoppingCartService.removeItem(missingCartId, PRODUCT.getId(), 1))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }

    @Test
    public void shouldThrowExceptionWhenAddingMissingProduct() {
        // GIVEN
        String missingProductId = "missing";

        // WHEN - THEN
        assertThatThrownBy(() -> shoppingCartService.addItem(CART_ID, missingProductId, 1))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void shouldThrowExceptionWhenRemovingMissingProduct() {
        // GIVEN
        String missingProductId = "missing";

        // WHEN - THEN
        assertThatThrownBy(() -> shoppingCartService.removeItem(CART_ID, missingProductId, 1))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    public void shouldThrowExceptionWhenGettingItemsFromMissingCart() {
        // GIVEN
        String missingCartId = "missing";

        // WHEN - THEN
        assertThatThrownBy(() -> shoppingCartService.getItems(missingCartId))
                .isInstanceOf(ShoppingCartNotFoundException.class);
    }
}