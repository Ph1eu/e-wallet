package com.project.service.address.mapper;

import com.project.api.resource.address.model.AddressResourceDto;
import com.project.api.resource.address.request.AddressCreateRequestDto;
import com.project.api.resource.address.request.AddressFilterRequestDto;
import com.project.api.resource.address.respond.PageAddressRespondDto;
import com.project.service.address.dto.AddressCreateDto;
import com.project.service.address.dto.AddressDto;
import com.project.service.address.dto.AddressFilterDto;
import com.project.service.address.dto.AddressPageDto;
import com.project.service.address.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
    @Named("addressDtoToAddressResourceDto")
    AddressResourceDto addressDtoToAddressResourceDto(AddressDto addressDto);
    @Named("addressFilterRequestToAddressFilterDto")
    AddressFilterDto addressFilterRequestToAddressFilterDto(AddressFilterRequestDto addressFilterRequestDto);
    @Mapping(target = "content", source = "content", qualifiedByName = "addressDtoToAddressResourceDto")
    PageAddressRespondDto addressPageDtoToPageAddressRespondDto(AddressPageDto addressDto);
    @Named("addressCreateRequestToAddressCreateDto")
    AddressCreateDto addressCreateRequestToAddressCreateDto(AddressCreateRequestDto addressCreateRequestDto);
}
