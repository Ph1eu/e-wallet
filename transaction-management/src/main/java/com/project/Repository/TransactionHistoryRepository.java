package com.project.Repository;

import com.project.Model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory,String> {
    @Query("SELECT th FROM TransactionHistory th WHERE th.transaction_date = :date")
    List<TransactionHistory> findTransactionHistoryByDate(Date date);
    @Query("SELECT th FROM TransactionHistory th WHERE th.recipient.idemail = :recipient")
    List<TransactionHistory> findTransactionHistoryByRecipient(String recipient);
    @Query("SELECT th FROM TransactionHistory th WHERE th.amount = :amount")
    List<TransactionHistory> findTransactionHistoryByAmount(int amount);
    @Query("SELECT th FROM TransactionHistory th WHERE th.sender.idemail = :sender")
    List<TransactionHistory> findTransactionHistoryBySender(String sender);
}
