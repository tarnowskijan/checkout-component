package org.shopping.endpoint.checkout;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.shopping.domain.checkout.ReceiptItem;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

class ReceiptItemDto {

    private final String productId;
    private final int quantity;
    private final String value;
    private final String currency;

    ReceiptItemDto(ReceiptItem item) {
        this.productId = item.getProduct().getId();
        this.quantity = item.getQuantity();
        MonetaryAmount value = item.getValue();
        this.value = value.getNumber().numberValue(BigDecimal.class).toPlainString();
        this.currency = value.getCurrency().getCurrencyCode();
    }

    private ReceiptItemDto(@JsonProperty("productId") String productId, @JsonProperty("quantity") int quantity,
                           @JsonProperty("value") String value, @JsonProperty("currency") String currency) {
        this.productId = productId;
        this.quantity = quantity;
        this.value = value;
        this.currency = currency;
    }

    String getProductId() {
        return productId;
    }

    int getQuantity() {
        return quantity;
    }

    String getValue() {
        return value;
    }

    String getCurrency() {
        return currency;
    }
}
