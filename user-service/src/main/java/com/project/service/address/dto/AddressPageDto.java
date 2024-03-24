package com.project.service.address.dto;

import com.project.service.address.entity.Address;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class AddressPageDto {
    @NonNull
    private Integer page;
    @NonNull
    private  Integer size;
    @NonNull
    private final List<AddressDto> content;
}
