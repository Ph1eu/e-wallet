package com.project.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Boolean existsByUsername(String username);
    Boolean existsByIdemail(String email);
    Optional<User> findByUsername(String username);
    @Query("SELECT u, b FROM User u INNER JOIN BalanceInformation b ON u.idemail = b.user Where b.balance_amount = :balance AND u.idemail = :userId")
    List<Object[]> findUserWithEmailandBalance(@Param("userId") String userId,@Param("balance") Integer balance);
    @Query("SELECT u, b FROM User u INNER JOIN BalanceInformation b ON u.idemail = b.user Where u.idemail = :userId ")
    List<Object[]> findUserWithEmail(@Param("userId") String userId);
    @Query("SELECT u, b FROM User u INNER JOIN BalanceInformation b ON u.idemail = b.user Where b.balance_amount = :balance ")
    List<Object[]> findUserWithBalance(@Param("balance") Integer balance);
    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.idemail = b.user")
    List<Object[]> findAllUsersWithBalanceInformation();
    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.idemail = b.user WHERE u.idemail = :userId")
    Optional<User> findUserWithBalanceInformation(@Param("userId") String userId);
    @Override
    Optional<User> findById(String s);
}
