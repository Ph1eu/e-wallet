package com.project.service_impl.paymentcard;

import com.project.service.paymentcard.entity.Paymentcard;
import com.project.service.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentcardRepository extends JpaRepository<Paymentcard, String>, JpaSpecificationExecutor<Paymentcard> {

    boolean existsByCard_number(String cardNumber);
}

