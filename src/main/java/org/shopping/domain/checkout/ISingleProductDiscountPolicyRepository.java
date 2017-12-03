package org.shopping.domain.checkout;

import java.util.List;

public interface ISingleProductDiscountPolicyRepository {
    List<SingleProductDiscountPolicy> findAll();
}
