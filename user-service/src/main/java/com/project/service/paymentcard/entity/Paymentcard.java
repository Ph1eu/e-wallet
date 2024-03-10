package com.project.service.paymentcard.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.service.user.entity.User;
import com.project.service.paymentcard.dto.PaymentcardDTO;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @Setter
    private User user;
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
    public Paymentcard(String id, String card_number, User user, String card_holder_name, String card_type, Date registration_date, Date expiration_date) {
        this.id = id;
        this.card_number = card_number;
        this.user = user;
        this.card_holder_name = card_holder_name;
        this.card_type = card_type;
        this.registration_date = registration_date;
        this.expiration_date = expiration_date;
    }

    public Paymentcard(PaymentcardDTO paymentcardDTO) {
        this.id = paymentcardDTO.getId();
        this.card_number = paymentcardDTO.getCard_number();
        this.user = null;
        this.card_holder_name = paymentcardDTO.getCard_holder_name();
        this.card_type = paymentcardDTO.getCard_type();
        this.registration_date = paymentcardDTO.getRegistration_date();
        this.expiration_date = paymentcardDTO.getExpiration_date();
    }

    public Paymentcard() {

    }

}
