package com.project.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.Payload.DTO.PaymentcardDTO;
import com.project.Payload.DTO.UserDTO;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="cards")
public class Paymentcard {
    @Id()
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

    public User getUser() {
        return user;
    }

    public void setUserWithDTO(UserDTO UserDTO) {
        this.user = new User(UserDTO);
    }
    public void setUser(User user) {
        this.user =user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paymentcard that = (Paymentcard) o;
        return Objects.equals(id, that.id) && Objects.equals(card_number, that.card_number) && Objects.equals(user, that.user) && Objects.equals(card_holder_name, that.card_holder_name) && Objects.equals(card_type, that.card_type) && Objects.equals(registration_date, that.registration_date) && Objects.equals(expiration_date, that.expiration_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, card_number, user, card_holder_name, card_type, registration_date, expiration_date);
    }

    @Override
    public String toString() {
        return "Paymentcard{" +
                "id='" + id + '\'' +
                ", card_number='" + card_number + '\'' +
                ", user=" + user.getIdemail() +
                ", card_holder_name='" + card_holder_name + '\'' +
                ", card_type='" + card_type + '\'' +
                ", registration_date=" + registration_date +
                ", expiration_date=" + expiration_date +
                '}';
    }
}
