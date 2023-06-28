package com.project.Repository;

import com.project.Model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory,String> {

    @Query("SELECT th FROM TransactionHistory th WHERE th.recipient.idemail = :recipient")
    List<TransactionHistory> findTransactionHistoryByRecipient(@Param("recipient")String recipient);
    @Query("SELECT th FROM TransactionHistory th WHERE th.amount = :amount")
    List<TransactionHistory> findTransactionHistoryByAmount(@Param("amount")int amount);
    @Query("SELECT th FROM TransactionHistory th WHERE th.sender.idemail = :sender")
    List<TransactionHistory> findTransactionHistoryBySender(@Param("sender") String sender);
    @Query("SELECT th FROM TransactionHistory th WHERE th.transaction_type = :type")
    List<TransactionHistory> findTransactionHistoryByTransaction_type(@Param("type")String type);
    @Query("SELECT th FROM TransactionHistory th WHERE th.transaction_date >= :startDate AND th.transaction_date <= :endDate AND th.transaction_type = :type")
    List<TransactionHistory> findTransactionHistoryByFilters(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("type") String type
    );
    @Query("SELECT th FROM TransactionHistory th WHERE th.transaction_date >= :startDate AND th.transaction_date <= :endDate ")
    List<TransactionHistory> findTransactionHistoryByRange(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
    @Query("SELECT th FROM TransactionHistory th WHERE th.transaction_date >= :startDate ")
    List<TransactionHistory> findTransactionHistoryByStartDate(
            @Param("startDate") Date startDate);
    @Query("SELECT th FROM TransactionHistory th WHERE th.transaction_date <= :endDate ")
    List<TransactionHistory> findTransactionHistoryByEndaDate(
            @Param("endDate") Date endDate);
}
