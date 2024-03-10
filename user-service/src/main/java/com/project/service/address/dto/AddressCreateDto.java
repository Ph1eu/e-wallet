package com.project.service.address.dto;

import lombok.Data;

@Data
public class AddressCreateDto {
    private String id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
