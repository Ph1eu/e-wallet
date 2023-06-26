package com.project.Payload.DTO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.Model.*;

import java.util.Date;
import java.util.Objects;

public class PaymentcardDTO {

    String id;

    String card_number;
    @JsonIgnore
    private UserDTO user;

    String card_holder_name;

    String card_type;

    Date registration_date;

    Date expiration_date;

    public PaymentcardDTO(String id, String card_number, UserDTO user, String card_holder_name, String card_type, Date registration_date, Date expiration_date) {
        this.id = id;
        this.card_number = card_number;
        this.user = user;
        this.card_holder_name = card_holder_name;
        this.card_type = card_type;
        this.registration_date = registration_date;
        this.expiration_date = expiration_date;
    }

    public PaymentcardDTO(String card_number, UserDTO user, String card_holder_name, String card_type, Date registration_date, Date expiration_date) {
        this.card_number = card_number;
        this.user = user;
        this.card_holder_name = card_holder_name;
        this.card_type = card_type;
        this.registration_date = registration_date;
        this.expiration_date = expiration_date;
    }
    public PaymentcardDTO(Paymentcard paymentcard) {
        this.id = paymentcard.getId();
        this.card_number = paymentcard.getCard_number();
//        if(paymentcard.getUser() == null){
//            this.user=null;
//        }
//        else{
//            this.user = new UserDTO(paymentcard.getUser());
//        }
        this.card_holder_name = paymentcard.getCard_holder_name();
        this.card_type = paymentcard.getCard_type();
        this.registration_date = paymentcard.getRegistration_date();
        this.expiration_date = paymentcard.getExpiration_date();
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public PaymentcardDTO() {

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
        PaymentcardDTO that = (PaymentcardDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(card_number, that.card_number) && Objects.equals(user, that.user) && Objects.equals(card_holder_name, that.card_holder_name) && Objects.equals(card_type, that.card_type) && Objects.equals(registration_date, that.registration_date) && Objects.equals(expiration_date, that.expiration_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, card_number, user, card_holder_name, card_type, registration_date, expiration_date);
    }
}
