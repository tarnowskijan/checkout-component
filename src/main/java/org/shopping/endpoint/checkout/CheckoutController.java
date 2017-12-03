package org.shopping.endpoint.checkout;

import org.shopping.domain.checkout.ICheckoutService;
import org.shopping.domain.checkout.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class CheckoutController {

    private final ICheckoutService checkoutService;

    @Autowired
    CheckoutController(ICheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @GetMapping(path = "/checkouts/{shoppingCartId}")
    @ResponseBody
    ReceiptDto checkout(@PathVariable String shoppingCartId) {
        Receipt receipt = checkoutService.checkout(shoppingCartId);
        return new ReceiptDto(receipt);
    }
}
