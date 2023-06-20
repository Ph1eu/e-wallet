package com.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="transaction_history")
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id",referencedColumnName = "id_email")
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id",referencedColumnName = "id_email")
    private User recipient;
    @Column(name= "transaction_type")
    private String transaction_type;
    @Column(name= "amount")
    private Long amount;
    @Column(name="transaction_date")
    private Date transaction_date;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
