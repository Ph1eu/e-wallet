package com.project.service.address.dto;


import lombok.Data;

@Data
public class AddressDto {
    private final String id;
    private final String street_address;

    private final String city;
    private final String province;
    private final String country;


}
