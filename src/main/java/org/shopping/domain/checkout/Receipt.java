package org.shopping.domain.checkout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Receipt {

    private final List<ReceiptItem> items;

    Receipt() {
        this.items = new ArrayList<>();
    }

    public List<ReceiptItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    void addItems(Collection<ReceiptItem> items) {
        this.items.addAll(items);
    }
}
