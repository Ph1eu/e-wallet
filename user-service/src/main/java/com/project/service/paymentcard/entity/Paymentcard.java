package com.project.service.paymentcard.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.service.user.entity.User;
import com.project.service.paymentcard.dto.PaymentcardDto;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Entity
@Table(name = "cards")
@EqualsAndHashCode
@ToString
public class Paymentcard {
    @Id()
    @Column(name = "card_id")
    private String id;
    @Column(name = "card_number", unique = true)
    @Setter
    private String card_number;
    @Column(name = "card_holder_name")
    @Setter
    private String card_holder_name;
    @Column(name = "card_type")
    @Setter
    private String card_type;
    @Column(name = "registration_date")
    @Setter
    private Date registration_date;
    @Column(name = "expiration_date")
    @Setter
    private Date expiration_date;



}
