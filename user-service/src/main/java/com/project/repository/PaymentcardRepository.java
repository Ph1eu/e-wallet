package com.project.repository;

import com.project.model.Paymentcard;
import com.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface PaymentcardRepository extends JpaRepository<Paymentcard,String> {
    List<Paymentcard> findAllByUser(User user);
    void deleteAllByUser(User user);
}

