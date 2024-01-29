package com.project.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Boolean existsByUsername(String username);
    Boolean existsByIdemail(String email);
    Optional<User> findByUsername(String username);
    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.idemail = b.user Where b.balance_amount = :balance AND u.idemail = :userId")
    Page<Object[]> findUserWithEmailandBalance(@Param("userId") String userId, @Param("balance") Integer balance, Pageable pageable);
    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.idemail = b.user Where u.idemail = :userId ")
    Page<Object[]> findUserWithEmail(@Param("userId") String userId,Pageable pageable);
    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.idemail = b.user Where b.balance_amount = :balance ")
    Page<Object[]> findUserWithBalance(@Param("balance") Integer balance,Pageable pageable);
    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.idemail = b.user")
    Page<Object[]> findAllUsersWithBalanceInformation(Pageable pageable);
    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.idemail = b.user WHERE u.idemail = :userId")
    Optional<User> findUserWithBalanceInformation(@Param("userId") String userId);
    @Override
    Optional<User> findById(String s);
}
