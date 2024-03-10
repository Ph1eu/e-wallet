package com.project.service_impl.balanceinformation;

import com.project.service.user.entity.User;
import com.project.service.balanceinformation.entity.BalanceInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BalanceInformationRepository extends JpaRepository<BalanceInformation, String> {
    BalanceInformation findBalanceInformationByUser(User user);

    //    @Query("SELECT b FROM BalanceInformation b WHERE b.phone_number = :phone")
//    BalanceInformation findBalanceInformationsByPhonenumber(String phone);
    @Query("SELECT b FROM BalanceInformation b WHERE b.user = :userid")
    Optional<BalanceInformation> findBalanceInformationsByUserId(@Param("userid") String userid);

}
