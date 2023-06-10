package com.project.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="cards")
public class Paymentcard {
    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="card_id")
    String id;
    @Column(name = "card_number",unique = true)
    String card_number;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference

    private User user;
    @Column(name="card_holder_name")
    String card_holder_name;
    @Column(name="card_type")
    String card_type;
    @Column(name = "registration_date")
    Date registration_date;
    @Column(name = "expiration_date")
    Date expiration_date;

    public Paymentcard(String card_number, User user, String card_holder_name, String card_type, Date registration_date, Date expiration_date) {
        this.card_number = card_number;
        this.user = user;
        this.card_holder_name = card_holder_name;
        this.card_type = card_type;
        this.registration_date = registration_date;
        this.expiration_date = expiration_date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Paymentcard() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_holder_name() {
        return card_holder_name;
    }

    public void setCard_holder_name(String card_holder_name) {
        this.card_holder_name = card_holder_name;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }
}
