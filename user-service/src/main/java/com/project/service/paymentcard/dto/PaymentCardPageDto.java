package com.project.service.paymentcard.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class PaymentCardPageDto {
    @NonNull
    private Integer page;
    @NonNull
    private Integer size;
    @NonNull
    private List<PaymentcardDto> content;
}
