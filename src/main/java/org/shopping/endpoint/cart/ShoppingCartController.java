package org.shopping.endpoint.cart;

import org.shopping.domain.cart.CartItem;
import org.shopping.domain.cart.IShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
class ShoppingCartController {

    private final IShoppingCartService shoppingCartService;

    @Autowired
    ShoppingCartController(IShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping(path = "/carts")
    @ResponseStatus(HttpStatus.CREATED)
    String createCart() {
        return shoppingCartService.createCart();
    }

    @PostMapping(path = "/carts/{cartId}/items/{productId}/{quantity}")
    void addItem(@PathVariable String cartId, @PathVariable String productId, @PathVariable int quantity) {
        shoppingCartService.addItem(cartId, productId, quantity);
    }

    @DeleteMapping(path = "/carts/{cartId}/items/{productId}/{quantity}")
    void removeItem(@PathVariable String cartId, @PathVariable String productId, @PathVariable int quantity) {
        shoppingCartService.removeItem(cartId, productId, quantity);
    }

    @GetMapping(path = "/carts/{cartId}")
    @ResponseBody
    Collection<CartItemDto> getItems(@PathVariable String cartId) {
        Collection<CartItem> cartItems = shoppingCartService.getItems(cartId);
        return cartItems.stream().map(CartItemDto::new).collect(Collectors.toList());
    }
}
