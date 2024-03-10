package com.project.payload.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.project.model.TransactionHistory;

import java.util.Date;
import java.util.Objects;

@JsonRootName("TransactionHistory")
public class TransactionHistoryDTO {


    private String id;

    private String senderid;

    private String recipientid;

    private String transaction_type;

    private int amount;

    private Date transaction_date;

    public TransactionHistoryDTO(String id, String senderid, String recipientid, String transaction_type, int amount, Date transaction_date) {
        this.id = id;
        this.senderid = senderid;
        this.recipientid = recipientid;
        this.transaction_type = transaction_type;
        this.amount = amount;
        this.transaction_date = transaction_date;
    }

    public TransactionHistoryDTO(String senderid, String recipientid, String transaction_type, int amount, Date transaction_date) {
        this.senderid = senderid;
        this.recipientid = recipientid;
        this.transaction_type = transaction_type;
        this.amount = amount;
        this.transaction_date = transaction_date;
    }

    public TransactionHistoryDTO(TransactionHistory transactionhistory) {
        this.id = transactionhistory.getId();
        this.senderid = transactionhistory.getSender().getId_email();
        this.recipientid = transactionhistory.getRecipient().getId_email();
        this.transaction_type = transactionhistory.getTransaction_type();
        this.amount = transactionhistory.getAmount();
        this.transaction_date = transactionhistory.getTransaction_date();
    }

    public TransactionHistoryDTO() {
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getRecipientid() {
        return recipientid;
    }

    public void setRecipientid(String recipientid) {
        this.recipientid = recipientid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionHistoryDTO that = (TransactionHistoryDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(senderid, that.senderid) && Objects.equals(recipientid, that.recipientid) && Objects.equals(transaction_type, that.transaction_type) && Objects.equals(amount, that.amount) && Objects.equals(transaction_date, that.transaction_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderid, recipientid, transaction_type, amount, transaction_date);
    }
}
