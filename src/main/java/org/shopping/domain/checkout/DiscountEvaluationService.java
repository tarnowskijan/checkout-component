package org.shopping.domain.checkout;

import org.shopping.domain.cart.CartItem;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
class DiscountEvaluationService {

    private final ISingleProductDiscountPolicyRepository singleProductDiscountPolicyRepository;

    DiscountEvaluationService(ISingleProductDiscountPolicyRepository singleProductDiscountPolicyRepository) {
        this.singleProductDiscountPolicyRepository = singleProductDiscountPolicyRepository;
    }

    List<ReceiptItem> evaluate(Collection<CartItem> cartItems) {
        List<SingleProductDiscountPolicy> singleProductDiscounts = getSingleProductDiscounts();
        if (singleProductDiscounts.isEmpty()) {
            return Collections.emptyList();
        }
        return evaluateSingleProductDiscounts(cartItems, singleProductDiscounts);
    }

    private List<SingleProductDiscountPolicy> getSingleProductDiscounts() {
        return singleProductDiscountPolicyRepository.findAll();
    }

    private List<ReceiptItem> evaluateSingleProductDiscounts(Collection<CartItem> cartItems, List<SingleProductDiscountPolicy> singleProductDiscounts) {
        List<ReceiptItem> receiptItems = new ArrayList<>();
        cartItems.forEach(item -> {
            Optional<SingleProductDiscountPolicy> applicableDiscount = findFirstApplicableDiscount(singleProductDiscounts, item);
            applicableDiscount.ifPresent(discount -> {
                ReceiptItem receiptItem = discount.evaluate(item);
                receiptItems.add(receiptItem);
            });
        });
        return receiptItems;
    }

    private Optional<SingleProductDiscountPolicy> findFirstApplicableDiscount(List<SingleProductDiscountPolicy> singleProductDiscounts, CartItem item) {
        return singleProductDiscounts.stream().filter(discount -> discount.isApplicable(item)).findFirst();
    }
}
