package org.shopping.domain.cart;

public class Product {

    private final String id;
    private final String name;

    Product(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
