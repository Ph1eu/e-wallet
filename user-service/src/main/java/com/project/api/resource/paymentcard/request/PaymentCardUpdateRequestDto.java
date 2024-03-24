package com.project.api.resource.paymentcard.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentCardUpdateRequestDto {
    @JsonProperty("card_number")
    private String card_number;
    @JsonProperty("card_holder_name")
    private String card_holder_name;
    @JsonProperty("card_type")
    private String card_type;
    @JsonProperty("registration_date")
    private Date registration_date;
    @JsonProperty("expiration_date")
    private Date expiration_date;
}
