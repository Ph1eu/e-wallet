package com.project.model;

import com.project.payload.dto.TransactionHistoryDTO;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "transaction_history")
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @JoinColumn(name = "sender_id", referencedColumnName = "id_email")
    private String sender;
    @JoinColumn(name = "recipient_id", referencedColumnName = "id_email")
    private String recipient;
    @Column(name = "transaction_type")
    private String transaction_type;
    @Column(name = "amount")
    private int amount;
    @Column(name = "transaction_date")
    private Date transaction_date;

    public TransactionHistory(String id, String sender, String recipient, String transaction_type, int amount, Date transaction_date) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.transaction_type = transaction_type;
        this.amount = amount;
        this.transaction_date = transaction_date;
    }

    public TransactionHistory(TransactionHistoryDTO transactionHistoryDTO) {
        this.id = transactionHistoryDTO.getId();
        this.transaction_type = transactionHistoryDTO.getTransaction_type();
        this.amount = transactionHistoryDTO.getAmount();
        this.transaction_date = transactionHistoryDTO.getTransaction_date();
    }

    public TransactionHistory() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
