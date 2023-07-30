package com.project.Repository;

import com.project.Model.Paymentcard;
import com.project.Model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface PaymentcardRepository extends JpaRepository<Paymentcard,String> {
    List<Paymentcard> findAllByUser(User user);
    void deleteAllByUser(User user);
}

