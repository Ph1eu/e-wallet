package com.project.api.rest.endpoints.address;

import com.project.api.common.model.ResponseEntityWrapper;
import com.project.api.resource.address.AddressResource;
import com.project.api.resource.address.model.AddressResourceDto;
import com.project.api.resource.address.request.AddressCreateRequestDto;
import com.project.api.resource.address.request.AddressFilterRequestDto;
import com.project.api.resource.address.request.AddressUpdateRequestDto;
import com.project.api.resource.address.respond.PageAddressRespondDto;
import com.project.service.address.dto.AddressCreateDto;
import com.project.service.address.dto.AddressDto;
import com.project.service.address.dto.AddressFilterDto;
import com.project.service.address.dto.AddressPageDto;
import com.project.service.address.entity.Address;
import com.project.service.address.mapper.AddressMapper;
import com.project.service_impl.address.AddressServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user/{userid}/address")
@RestController
public class AddressEndpoint implements AddressResource {
    private final Logger logger = LoggerFactory.getLogger(AddressEndpoint.class);
    @Autowired
    private AddressServiceImpl addressService;
    @Autowired
    private AddressMapper addressMapper;
    @Override
    @PreAuthorize("#userid == authentication.principal.id")
    @Transactional(readOnly = true)
    public ResponseEntityWrapper<AddressResourceDto> getByID(String id, String userid) {
        logger.info("Request to get address with id: {}", id);
        Address address = addressService.getById(id);
        AddressDto addressDto = addressMapper.addressToAddressDto(address);
        AddressResourceDto addressResourceDto = addressMapper.addressDtoToAddressResourceDto(addressDto);
        logger.debug("Successfully retrieved address with id: {}", id);
        return ResponseEntityWrapper.<AddressResourceDto>builder().message("Address found").data(addressResourceDto).build();
    }
    @Override
    @PreAuthorize("#userid == authentication.principal.id")
    @Transactional(readOnly = true)
    public ResponseEntityWrapper<PageAddressRespondDto> list(String userid, AddressFilterRequestDto addressFilterRequestDto) {
        logger.info("Request to get address list with filter: {}", addressFilterRequestDto.toString());
        AddressFilterDto addressFilterDto = addressMapper.addressFilterRequestToAddressFilterDto(addressFilterRequestDto);
        AddressPageDto addressDto = addressService.list(addressFilterDto);
        PageAddressRespondDto pageAddressRespondDto = addressMapper.addressPageDtoToPageAddressRespondDto(addressDto);
        logger.debug("Successfully retrieved address list with filter: {}", addressFilterRequestDto);
        return ResponseEntityWrapper.<PageAddressRespondDto>builder().message("Address list").data(pageAddressRespondDto).build();
    }

    @Override
    @PreAuthorize("#userid == authentication.principal.id")
    @Transactional
    public ResponseEntityWrapper<AddressResourceDto> create(AddressCreateRequestDto addressCreateRequestDto, String userid) {
        logger.info("Request to create address with :{}", addressCreateRequestDto.toString());
        AddressCreateDto addressCreateDto = addressMapper.addressCreateRequestToAddressCreateDto(addressCreateRequestDto);
        Address address = addressService.create(addressCreateDto);
        AddressDto addressDto = addressMapper.addressToAddressDto(address);
        AddressResourceDto addressResourceDto = addressMapper.addressDtoToAddressResourceDto(addressDto);
        logger.debug("Successfully created address with id: {}", addressDto.id());
        return ResponseEntityWrapper.<AddressResourceDto>builder().message("Address created").data(addressResourceDto).build();
    }

    @Override
    @PreAuthorize("#userid == authentication.principal.id")
    @Transactional
    public ResponseEntityWrapper<AddressResourceDto> update(String id, AddressUpdateRequestDto addressCreateRequestDto, String userid) {
        return null;
    }

    @Override
    public ResponseEntityWrapper<AddressResourceDto> delete(String id, String userid) {
        return null;
    }
}
