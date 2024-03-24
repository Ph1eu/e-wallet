package com.project.service.address.mapper;

import com.project.service.address.dto.AddressCreateDto;
import com.project.service.address.dto.AddressDto;
import com.project.service.address.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);
    @Named("addressToAddressDto")
    AddressDto addressToAddressDto(Address address);
    @Named("addressDtoToAddress")
    Address addressDtoToAddress(AddressDto addressDto);
    @Named("addressCreateDtoToAddress")
    Address addressCreateDtoToAddress(AddressCreateDto addressCreateDto);

}
