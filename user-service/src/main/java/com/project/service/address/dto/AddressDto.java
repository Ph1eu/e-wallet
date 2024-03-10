package com.project.service.address.dto;


import lombok.Data;

@Data
public class AddressDto {
    private final String id;
    private final String Street_address;

    private final String City;
    private final String province;
    private final String country;


}
