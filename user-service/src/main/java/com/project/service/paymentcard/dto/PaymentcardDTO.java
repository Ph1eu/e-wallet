package com.project.service.paymentcard.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.service.paymentcard.entity.Paymentcard;
import com.project.service.user.dto.UserDto;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentcardDTO {

    private String id;

    private String card_number;
    private String card_holder_name;
    private String card_type;
    private Date registration_date;
    private Date expiration_date;
    @JsonIgnore
    private UserDto user;

    public PaymentcardDTO(String id, String card_number, UserDto user, String card_holder_name, String card_type, Date registration_date, Date expiration_date) {
        this.id = id;
        this.card_number = card_number;
        this.user = user;
        this.card_holder_name = card_holder_name;
        this.card_type = card_type;
        this.registration_date = registration_date;
        this.expiration_date = expiration_date;
    }

    public PaymentcardDTO(String card_number, UserDto user, String card_holder_name, String card_type, Date registration_date, Date expiration_date) {
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
}
