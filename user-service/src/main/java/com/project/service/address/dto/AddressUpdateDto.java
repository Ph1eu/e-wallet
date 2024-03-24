package com.project.service.address.dto;

import lombok.Data;

public record AddressUpdateDto(String street, String city, String province, String country) {
}
