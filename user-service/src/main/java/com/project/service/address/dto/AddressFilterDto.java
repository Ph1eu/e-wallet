package com.project.service.address.dto;

import lombok.Data;

@Data
public class AddressFilterDto {
    private String street;
    private String city;
    private String province;
    private String country;
    private int page;
    private int size;
}
