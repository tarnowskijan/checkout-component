package org.shopping.domain.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
class ShoppingCartService implements IShoppingCartService {

    private final ShoppingCartFactory shoppingCartFactory;
    private final IShoppingCartRepository shoppingCartRepository;
    private final IProductRepository productRepository;

    @Autowired
    ShoppingCartService(ShoppingCartFactory shoppingCartFactory, IShoppingCartRepository shoppingCartRepository,
                        IProductRepository productRepository) {
        this.shoppingCartFactory = shoppingCartFactory;
        this.shoppingCartRepository = shoppingCartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public String createCart() {
        ShoppingCart cart = shoppingCartFactory.create();
        shoppingCartRepository.save(cart);
        return cart.getId();
    }

    @Override
    public void addItem(String cartId, String productId, int quantity) throws ShoppingCartNotFoundException, ProductNotFoundException {
        ShoppingCart shoppingCart = findShoppingCart(cartId);
        Product product = findProduct(productId);
        shoppingCart.addItem(product, quantity);
    }

    @Override
    public void removeItem(String cartId, String productId, int quantity) throws ShoppingCartNotFoundException, ProductNotFoundException {
        ShoppingCart shoppingCart = findShoppingCart(cartId);
        Product product = findProduct(productId);
        shoppingCart.removeItem(product, quantity);
    }

    @Override
    public Collection<CartItem> getItems(String cartId) {
        return findShoppingCart(cartId).getItems();
    }

    private ShoppingCart findShoppingCart(String cartId) {
        return shoppingCartRepository.findById(cartId)
                .orElseThrow(ShoppingCartNotFoundException::new);
    }

    private Product findProduct(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }
}
