package com.project.Payload.Request.CRUDUserInforRequest;

public class PaymentcardCRUD {
    String card_number;
    String card_holder_name;
    String card_type;
    String registration_date;
    String expiration_date;

    public PaymentcardCRUD(String card_number, String card_holder_name, String card_type, String registration_date, String expiration_date) {
        this.card_number = card_number;
        this.card_holder_name = card_holder_name;
        this.card_type = card_type;
        this.registration_date = registration_date;
        this.expiration_date = expiration_date;
    }

    public PaymentcardCRUD() {
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

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

}
