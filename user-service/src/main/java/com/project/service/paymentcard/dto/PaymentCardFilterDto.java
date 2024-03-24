package com.project.service.paymentcard.dto;

import lombok.Data;

public record PaymentCardFilterDto(String card_type, String user_id, int page, int size) {
}
