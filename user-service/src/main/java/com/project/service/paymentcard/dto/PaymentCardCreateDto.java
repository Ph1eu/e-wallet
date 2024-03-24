package com.project.service.paymentcard.dto;

import lombok.Data;

import java.util.Date;

public record PaymentCardCreateDto(String id, String card_number, String card_holder_name, String card_type,
                                   Date registration_date, Date expiration_date) {
}
