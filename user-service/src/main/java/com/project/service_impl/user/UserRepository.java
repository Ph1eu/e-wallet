package com.project.service_impl.user;

import com.project.service.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.email = b.user Where b.balance_amount = :balance AND u.email = :userId")
    Page<Object[]> findUserWithEmailandBalance(@Param("userId") String userId, @Param("balance") Integer balance, Pageable pageable);

    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.email = b.user Where u.email = :userId ")
    Page<Object[]> findUserWithEmail(@Param("userId") String userId, Pageable pageable);

    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.email = b.user Where b.balance_amount = :balance ")
    Page<Object[]> findUserWithBalance(@Param("balance") Integer balance, Pageable pageable);

    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.email = b.user")
    Page<Object[]> findAllUsersWithBalanceInformation(Pageable pageable);

    @Query("SELECT u, b FROM User u LEFT JOIN BalanceInformation b ON u.email = b.user WHERE u.email = :userId")
    Optional<User> findUserWithBalanceInformation(@Param("userId") String userId);

    @Override
    Optional<User> findById(String s);
}
