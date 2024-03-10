package com.project.service.balanceinformation.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
@Table(name = "balance_information")
@ToString
@EqualsAndHashCode
public class BalanceInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "user_id")
    @Setter
    private String user;
    @Column(name = "balance_amount")
    @Setter
    private int balance_amount;
    @Column
    @Setter
    private String phone_number;

    public BalanceInformation(String id, String user, int balance_amount, String phone_number) {
        this.id = id;
        this.user = user;
        this.balance_amount = balance_amount;
        this.phone_number = phone_number;
    }

    public BalanceInformation() {

    }

}
