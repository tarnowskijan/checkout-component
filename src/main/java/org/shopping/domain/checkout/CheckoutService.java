package org.shopping.domain.checkout;

import org.shopping.domain.cart.CartItem;
import org.shopping.domain.cart.IShoppingCartService;
import org.shopping.domain.cart.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Service
class CheckoutService implements ICheckoutService {

    private final IShoppingCartService shoppingCartService;
    private final IProductPricingRepository productPricingRepository;

    @Autowired
    CheckoutService(IShoppingCartService shoppingCartService, IProductPricingRepository productPricingRepository) {
        this.shoppingCartService = shoppingCartService;
        this.productPricingRepository = productPricingRepository;
    }

    @Override
    public Receipt checkout(String shoppingCartId) {
        Collection<CartItem> cartItems = shoppingCartService.getItems(shoppingCartId);
        Map<Product, ProductPricing> priceByProduct = getProductPrices(cartItems);
        return createReceipt(cartItems, priceByProduct);
    }

    private Map<Product, ProductPricing> getProductPrices(Collection<CartItem> cartItems) {
        return cartItems.stream().map(CartItem::getProduct).collect(Collectors.toMap(identity(),
                productPricingRepository::findByProduct));
    }

    private Receipt createReceipt(Collection<CartItem> cartItems, Map<Product, ProductPricing> priceByProduct) {
        Receipt receipt = new Receipt();
        cartItems.forEach(item -> {
            Product product = item.getProduct();
            receipt.registerItem(item, priceByProduct.get(product));
        });
        return receipt;
    }
}
