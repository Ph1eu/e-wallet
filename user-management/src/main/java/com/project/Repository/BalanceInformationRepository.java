package com.project.Repository;

import com.project.Model.BalanceInformation;
import com.project.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BalanceInformationRepository extends JpaRepository<BalanceInformation,String> {
    BalanceInformation findBalanceInformationByUser(User user);
//    @Query("SELECT b FROM BalanceInformation b WHERE b.phone_number = :phone")
//    BalanceInformation findBalanceInformationsByPhonenumber(String phone);
    @Query("SELECT b FROM BalanceInformation b WHERE b.user = :userid")
    Optional<BalanceInformation> findBalanceInformationsByUserId(@Param("userid")String userid);

}
