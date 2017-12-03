package org.shopping;

import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Scope("cucumber-glue")
public class AcceptanceTestState {

    private ResponseEntity<?> latestResponse;
    private String latestShoppingCartId;

    public ResponseEntity<?> getLatestResponse() {
        return latestResponse;
    }

    public void setLatestResponse(ResponseEntity<?> latestResponse) {
        this.latestResponse = latestResponse;
    }

    public String getLatestShoppingCartId() {
        return latestShoppingCartId;
    }

    public void setLatestShoppingCartId(String latestShoppingCartId) {
        this.latestShoppingCartId = latestShoppingCartId;
    }
}
