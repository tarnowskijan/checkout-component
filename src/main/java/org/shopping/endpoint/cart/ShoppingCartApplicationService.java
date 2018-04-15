package org.shopping.endpoint.cart;

import org.shopping.domain.cart.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
class ShoppingCartApplicationService {

    private final IShoppingCartRepository shoppingCartRepository;
    private final IProductRepository productRepository;

    @Autowired
    ShoppingCartApplicationService(IShoppingCartRepository shoppingCartRepository, IProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
    }

    String createCart() {
        ShoppingCart cart = ShoppingCartFactory.create();
        shoppingCartRepository.save(cart);
        return cart.getId();
    }

    void addItem(String cartId, String productId, int quantity) throws ShoppingCartNotFoundException, ProductNotFoundException {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId);
        Product product = findProduct(productId);
        shoppingCart.addItem(product, quantity);
    }

    void removeItem(String cartId, String productId, int quantity) throws ShoppingCartNotFoundException, ProductNotFoundException {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId);
        Product product = findProduct(productId);
        shoppingCart.removeItem(product, quantity);
    }

    public Collection<CartItem> getItems(String cartId) {
        return shoppingCartRepository.findById(cartId).getItems();
    }

    private Product findProduct(String productId) {
        return productRepository.findById(productId);
    }
}
