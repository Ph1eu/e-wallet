package com.project.service.address.dto;

import lombok.Data;

public record AddressCreateDto(String id, String street, String city, String province, String country) {
}
