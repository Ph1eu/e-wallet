package com.project.Repository;

import com.project.Model.BalanceInformation;
import com.project.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BalanceInformationRepository extends JpaRepository<BalanceInformation,String> {
    BalanceInformation findBalanceInformationByUser(User user);
    @Query("SELECT b FROM BalanceInformation b WHERE b.phone_number = :phone")

    BalanceInformation findBalanceInformationsByPhonenumber(String phone);
}
