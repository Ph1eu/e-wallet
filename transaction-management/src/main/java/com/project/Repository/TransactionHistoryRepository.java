package com.project.Repository;

import com.project.Model.TransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory,String> {

    @Query("SELECT th FROM TransactionHistory th WHERE th.recipient.idemail = :recipient")
    Page<TransactionHistory> findTransactionHistoryByRecipient(@Param("recipient")String recipient, Pageable pageable);
    @Query("SELECT th FROM TransactionHistory th WHERE th.amount = :amount")
    List<TransactionHistory> findTransactionHistoryByAmount(@Param("amount")int amount);
    @Query("SELECT th FROM TransactionHistory th WHERE th.sender.idemail = :sender")
    List<TransactionHistory> findTransactionHistoryBySender(@Param("sender") String sender);
    @Query("SELECT th FROM TransactionHistory th WHERE th.transaction_type = :type")
    Page<TransactionHistory> findTransactionHistoryByTransactionType(@Param("type")String type,Pageable pageable);
//    @Query(value = "SELECT * FROM transaction_history ORDER BY id ASC LIMIT :pageSize OFFSET :offset",
//            countQuery = "SELECT COUNT(*) FROM transaction_history",
//            nativeQuery = true)
//    List<TransactionHistory> findAllTransactionHistory(@Param("pageSize") int pageSize, @Param("offset") int offset);

    Page<TransactionHistory> findAll(Pageable pageable);
    @Query("SELECT t FROM TransactionHistory t")
    Page<TransactionHistory> findAllTransactionHistory(Pageable pageable);
    @Query("SELECT th FROM TransactionHistory th WHERE th.transaction_date >= :startDate AND th.transaction_date <= :endDate AND th.transaction_type = :type")
    Page<TransactionHistory> findTransactionHistoryByFilters(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("type") String type,
            Pageable pageable
    );
    @Query("SELECT th FROM TransactionHistory th WHERE th.transaction_date >= :startDate AND th.transaction_date <= :endDate ")
    Page<TransactionHistory> findTransactionHistoryByRange(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable);
    @Query("SELECT th FROM TransactionHistory th WHERE th.transaction_date >= :startDate ")
    Page<TransactionHistory> findTransactionHistoryByStartDate(
            @Param("startDate") Date startDate,
            Pageable pageable);
    @Query("SELECT th FROM TransactionHistory th WHERE th.transaction_date <= :endDate ")
    Page<TransactionHistory> findTransactionHistoryByEndDate(
            @Param("endDate") Date endDate,
            Pageable pageable);

}
