package com.project.service_impl.paymentcard;

import com.project.service.paymentcard.entity.Paymentcard;
import org.springframework.data.jpa.domain.Specification;

public class PaymentCardSpecification {
    public static Specification<Paymentcard> hasCardType(String cardType) {
        return (root, query, cb) -> cardType == null ? null : cb.equal(root.get("card_type"), cardType);
    }
    public static Specification<Paymentcard> hasUserId(String userId) {
        return (root, query, cb) -> userId == null ? null : cb.equal(root.get("user").get("id"), userId);
    }
}
