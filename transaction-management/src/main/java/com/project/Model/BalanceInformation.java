package com.project.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.Payload.DTO.BalanceInformationDTO;
import jakarta.persistence.*;

@Entity
@Table(name="balance_information")
public class BalanceInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "id_email")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;
    @Column(name="balance_amount")
    private long balance_amount;
    @Column
    private String phone_number;

    public BalanceInformation(String id, User user, long balance_amount, String phone_number) {
        this.id = id;
        this.user = user;
        this.balance_amount = balance_amount;
        this.phone_number = phone_number;
    }

    public BalanceInformation() {
    }

    public BalanceInformation(BalanceInformationDTO balanceInformationDTO) {
        this.id = balanceInformationDTO.getId();
        this.balance_amount = balanceInformationDTO.getBalance_amount();
        this.phone_number = balanceInformationDTO.getPhone_number();
    }

    public BalanceInformation(User user, long balance_amount, String phone_number) {
        this.user = user;
        this.balance_amount = balance_amount;
        this.phone_number = phone_number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(long balance_amount) {
        this.balance_amount = balance_amount;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
