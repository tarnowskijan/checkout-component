package org.shopping.endpoint.checkout;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.shopping.domain.checkout.Receipt;
import org.shopping.domain.checkout.ReceiptItem;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class ReceiptDto {

    private final List<ReceiptItemDto> items;

    ReceiptDto(Receipt receipt) {
        List<ReceiptItem> receiptItems = receipt.getItems();
        this.items = receiptItems.stream().map(ReceiptItemDto::new).collect(Collectors.toList());
    }

    private ReceiptDto(@JsonProperty("items") List<ReceiptItemDto> items) {
        this.items = items;
    }

    List<ReceiptItemDto> getItems() {
        return Collections.unmodifiableList(items);
    }
}
