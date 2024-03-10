package com.project.repository;

import com.project.model.BalanceInformation;
import com.project.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BalanceInformationRepository extends JpaRepository<BalanceInformation, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BalanceInformation> findBalanceInformationByUser(User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BalanceInformation b WHERE b.phone_number = :phone")
    Optional<BalanceInformation> findBalanceInformationsByPhonenumber(String phone);

    @Modifying
    @Query("UPDATE BalanceInformation bi SET bi.balance_amount = :newBalance WHERE bi.id = :id")
    void updateBalanceWithID(@Param("id") String id, @Param("newBalance") Integer newBalance);

    @Modifying
    @Query("UPDATE BalanceInformation bi SET bi.balance_amount = :newBalance WHERE bi.phone_number = :phone")
    void updateBalanceWithPhone(@Param("phone") String phone, @Param("newBalance") Integer newBalance);

}
