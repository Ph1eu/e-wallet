package com.project.service.paymentcard.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentCardCreateDto {
    private String id;
    private String card_number;
    private String card_holder_name;
    private String card_type;
    private Date registration_date;
    private Date expiration_date;
}
