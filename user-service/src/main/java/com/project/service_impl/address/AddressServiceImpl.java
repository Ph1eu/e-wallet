package com.project.service_impl.address;

import com.project.service.address.AddressService;
import com.project.service.address.dto.AddressCreateDto;
import com.project.service.address.dto.AddressFilterDto;
import com.project.service.address.dto.AddressPageDto;
import com.project.service.address.dto.AddressUpdateDto;
import com.project.service.address.entity.Address;
import com.project.service.address.mapper.AddressMapper;
import com.project.service_impl.paymentcard.PaymentcardRepository;
import com.project.service_impl.user.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressMapper addressMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Address> findById(String id) {
        logger.info("Request to find address by id: {}", id);
        Optional<Address> address = addressRepository.findById(id);
        logger.debug("Successfully Found address: {}", address);
        return address;
    }

    @Override
    @Transactional(readOnly = true)
    public Address getById(String id) {
        logger.info("Request to get address by id: {}", id);
        Address address = findById(id).orElseThrow(() -> new EntityNotFoundException("Address not found");
        logger.debug("Successfully got address: {}", address);
        return address;
    }

    @Override
    @Transactional
    public Address create(AddressCreateDto addressCreateDto) {
        logger.info("Request to create address: {}", addressCreateDto);
        if(existsById(addressCreateDto.id())) {
            throw new EntityExistsException("Address already exists");
        }
        Address address = addressMapper.addressCreateDtoToAddress(addressCreateDto);
        addressRepository.save(address);
        logger.debug("Successfully created address: {}", address);
        return address;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        logger.info("Request to delete address by id: {}", id);
        addressRepository.deleteById(id);
        logger.debug("Successfully deleted address by id: {}", id);
    }

    @Override
    @Transactional
    public void update(String id, AddressUpdateDto addressUpdateDto) {
        logger.info("Request to update address by id: {}", id);
        Address address = getById(id);
        if(addressUpdateDto.city() != null) {
            address.setCity(addressUpdateDto.city());
        }
        if(addressUpdateDto.country() != null) {
            address.setCountry(addressUpdateDto.country());
        }
        if(addressUpdateDto.street() != null) {
            address.setStreet(addressUpdateDto.street());
        }
        if(addressUpdateDto.province() != null) {
            address.setProvince(addressUpdateDto.province());
        }
        addressRepository.save(address);
        logger.debug("Successfully updated address by id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        logger.info("Request to check if address exists by id: {}", id);
        boolean exists = addressRepository.existsById(id);
        logger.debug("Address exists by id: {}", exists);
        return exists;
    }

    @Override
    @Transactional(readOnly = true)
    public AddressPageDto list(AddressFilterDto filter) {
        logger.info("Request to list addresses by filter: {}", filter);
        Specification<Address> spec = Specification.where(AddressSpecifications.withStreet(filter.getStreet()))
                .and(AddressSpecifications.withCity(filter.getCity()))
                .and(AddressSpecifications.withProvince(filter.getProvince()))
                .and(AddressSpecifications.withCountry(filter.getCountry()));
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        Page<Address> addresses = addressRepository.findAll(spec, pageable);
        AddressPageDto addressPageDto = new AddressPageDto(addresses.getNumber(), addresses.getSize(), addresses.getContent().stream().map(addressMapper::addressToAddressDto).toList());
        logger.debug("Successfully listed addresses by filter: {}", filter);
        return addressPageDto;
    }
}
