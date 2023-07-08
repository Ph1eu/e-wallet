package com.project.Repository;

import com.project.Model.BalanceInformation;
import com.project.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BalanceInformationRepository extends JpaRepository<BalanceInformation,String> {
    BalanceInformation findBalanceInformationByUser(User user);
    @Query("SELECT b FROM BalanceInformation b WHERE b.phone_number = :phone")

    BalanceInformation findBalanceInformationsByPhonenumber(String phone);
    @Modifying
    @Query("UPDATE BalanceInformation bi SET bi.balance_amount = :newBalance WHERE bi.id = :id")
    void updateBalanceWithID(@Param("id") String id, @Param("newBalance") Integer newBalance);
    @Modifying
    @Query("UPDATE BalanceInformation bi SET bi.balance_amount = :newBalance WHERE bi.phone_number = :phone")
    void updateBalanceWithPhone(@Param("phone") String phone, @Param("newBalance") Integer newBalance);

}
