package com.project.api.resource.paymentcard.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record PaymentCardResourceDto(
        @JsonProperty("Id") String id,
        @JsonProperty("Card_number") String card_number,
        @JsonProperty("Card_holder_name") String card_holder_name,
        @JsonProperty("Card_type") String card_type,
        @JsonProperty("Registration_date") Date registration_date,
        @JsonProperty("Expiration_date") Date expiration_date
) {
}
