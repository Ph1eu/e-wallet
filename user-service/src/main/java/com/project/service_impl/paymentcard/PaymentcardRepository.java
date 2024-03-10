package com.project.service_impl.paymentcard;

import com.project.service.paymentcard.entity.Paymentcard;
import com.project.service.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentcardRepository extends JpaRepository<Paymentcard, String> {
    List<Paymentcard> findAllByUser(User user);

    void deleteAllByUser(User user);
}

