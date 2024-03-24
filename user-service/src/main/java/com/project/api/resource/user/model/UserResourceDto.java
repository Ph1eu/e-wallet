package com.project.api.resource.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.api.resource.address.model.AddressResourceDto;
import com.project.api.resource.paymentcard.model.PaymentCardResourceDto;

import java.util.Date;
import java.util.List;


public record UserResourceDto(@JsonProperty("id") String id, @JsonProperty("email") String email,
                              @JsonProperty("username") String username, @JsonProperty("password") String password,
                              @JsonProperty("first_name") String first_name,
                              @JsonProperty("last_name") String last_name,
                              @JsonProperty("registration_date") Date registration_date,
                              @JsonProperty("address") AddressResourceDto address,
                              @JsonProperty("payment_cards") List<PaymentCardResourceDto> paymentCards,
                              @JsonProperty("roles") String roles) {
}
