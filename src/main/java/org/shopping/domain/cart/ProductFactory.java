package org.shopping.domain.cart;

import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
public class ProductFactory {

    public Product create(String id, String name) {
        return new Product(requireNonNull(id), requireNonNull(name));
    }
}
