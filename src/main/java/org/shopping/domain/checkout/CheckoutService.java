package org.shopping.domain.checkout;

import org.shopping.domain.cart.CartItem;
import org.shopping.domain.cart.Product;
import org.shopping.domain.cart.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
class CheckoutService implements ICheckoutService {

    private final IProductPricingRepository productPricingRepository;
    private final DiscountEvaluationService discountEvaluationService;

    @Autowired
    CheckoutService(IProductPricingRepository productPricingRepository,
                    DiscountEvaluationService discountEvaluationService) {
        this.productPricingRepository = productPricingRepository;
        this.discountEvaluationService = discountEvaluationService;
    }

    @Override
    public Receipt checkout(ShoppingCart shoppingCart) {
        Map<Product, CartItem> cartItemsByProduct = getCartItems(shoppingCart);
        List<ReceiptItem> discountedReceiptItems = discountEvaluationService.evaluate(cartItemsByProduct.values());
        decreaseQuantityOfDiscountedItems(cartItemsByProduct, discountedReceiptItems);
        List<ReceiptItem> standardReceiptItems = evaluateStandardPrices(cartItemsByProduct);
        return createReceipt(discountedReceiptItems, standardReceiptItems);
    }

    private Map<Product, CartItem> getCartItems(ShoppingCart shoppingCart) {
        return shoppingCart.getItems().stream().collect(toMap(CartItem::getProduct, identity()));
    }

    private void decreaseQuantityOfDiscountedItems(Map<Product, CartItem> cartItemsByProduct, List<ReceiptItem> discountedReceiptItems) {
        discountedReceiptItems.forEach(receiptItem -> decreaseQuantity(receiptItem, cartItemsByProduct));
    }

    private void decreaseQuantity(ReceiptItem receiptItem, Map<Product, CartItem> cartItemsByProduct) {
        Product product = receiptItem.getProduct();
        CartItem cartItem = cartItemsByProduct.get(product);
        CartItem unevaluatedItem = cartItem.withDecreasedQuantity(receiptItem.getQuantity());
        if (unevaluatedItem.isNotEmpty()) {
            cartItemsByProduct.put(product, unevaluatedItem);
        } else {
            cartItemsByProduct.remove(product);
        }
    }

    private List<ReceiptItem> evaluateStandardPrices(Map<Product, CartItem> cartItemsByProduct) {
        Map<Product, ProductPricing> priceByProduct = getProductPrices(cartItemsByProduct.keySet());
        return cartItemsByProduct.values().stream().map((item) -> toReceiptItem(item, priceByProduct.get(item.getProduct())))
                .collect(Collectors.toList());
    }

    private ReceiptItem toReceiptItem(CartItem item, ProductPricing productPricing) {
        return new ReceiptItem(item.getProduct(), item.getQuantity(), productPricing.getPrice().multiply(item.getQuantity()));
    }

    private Map<Product, ProductPricing> getProductPrices(Set<Product> products) {
        return products.stream().collect(toMap(identity(), productPricingRepository::findByProduct));
    }

    private Receipt createReceipt(List<ReceiptItem> discountedReceiptItems, List<ReceiptItem> standardReceiptItems) {
        Receipt receipt = new Receipt();
        receipt.addItems(discountedReceiptItems);
        receipt.addItems(standardReceiptItems);
        return receipt;
    }
}