package org.shopping.domain.cart;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableCollection;

public class ShoppingCart {

    private final String id;
    private final Map<String, CartItem> itemsByProductId = new ConcurrentHashMap<>();

    ShoppingCart(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addItem(Product product, int quantity) {
        Optional<CartItem> existingItem = findItemForProduct(product);
        CartItem item = existingItem
                .map(i -> i.withIncreasedQuantity(quantity))
                .orElse(CartItem.create(product, quantity));
        saveItem(item);
    }

    public void removeItem(Product product, int quantity) {
        findItemForProduct(product)
                .map(i -> i.withDecreasedQuantity(quantity))
                .ifPresent(this::saveItem);
    }

    public Collection<CartItem> getItems() {
        return unmodifiableCollection(itemsByProductId.values());
    }

    private Optional<CartItem> findItemForProduct(Product product) {
        return Optional.ofNullable(itemsByProductId.get(product.getId()));
    }

    private void saveItem(CartItem item) {
        Product product = item.getProduct();
        if (item.isNotEmpty()) {
            itemsByProductId.put(product.getId(), item);
        } else {
            itemsByProductId.remove(product.getId());
        }
    }
}
