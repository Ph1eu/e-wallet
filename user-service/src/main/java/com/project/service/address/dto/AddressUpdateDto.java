package com.project.service.address.dto;

import lombok.Data;

@Data
public class AddressUpdateDto {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
