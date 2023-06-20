package com.project.Repository;

import com.project.Model.BalanceInformation;
import com.project.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanaceInformationRepository extends JpaRepository<BalanceInformation,String> {
    BalanceInformation findBalanceInformationByUser(User user);
}
