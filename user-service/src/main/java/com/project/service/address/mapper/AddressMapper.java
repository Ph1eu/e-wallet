package com.project.service.address.mapper;

import com.project.service.address.dto.AddressDto;
import com.project.service.address.entity.Address;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);
    @Named("addressToAddressDto")
    AddressDto addressToAddressDto(Address address);
    @Named("addressDtoToAddress")
    Address addressDtoToAddress(AddressDto addressDto);

}
