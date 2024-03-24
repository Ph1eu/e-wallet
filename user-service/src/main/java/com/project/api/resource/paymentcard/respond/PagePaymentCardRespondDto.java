package com.project.api.resource.paymentcard.respond;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.api.resource.paymentcard.model.PaymentCardResourceDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class PagePaymentCardRespondDto {
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("content")
    private List<PaymentCardResourceDto> content;

}
