package org.shopping.domain.cart;

import static java.util.Objects.requireNonNull;

public final class ProductFactory {
    private ProductFactory() {
    }

    public static Product create(String id, String name) {
        return new Product(requireNonNull(id), requireNonNull(name));
    }
}
