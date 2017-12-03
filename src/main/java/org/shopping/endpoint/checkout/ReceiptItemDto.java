package org.shopping.endpoint.checkout;

import org.shopping.domain.checkout.ReceiptItem;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

class ReceiptItemDto {

    private final String productId;
    private final int quantity;
    private final BigDecimal value;
    private final String currency;

    ReceiptItemDto(ReceiptItem item) {
        this.productId = item.getProduct().getId();
        this.quantity = item.getQuantity();
        MonetaryAmount value = item.getValue();
        this.value = value.getNumber().numberValue(BigDecimal.class);
        this.currency = value.getCurrency().getCurrencyCode();
    }

    String getProductId() {
        return productId;
    }

    int getQuantity() {
        return quantity;
    }

    BigDecimal getValue() {
        return value;
    }

    String getCurrency() {
        return currency;
    }
}
