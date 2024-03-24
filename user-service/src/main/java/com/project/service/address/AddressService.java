package com.project.service.address;

import com.project.service.address.dto.*;
import com.project.service.address.entity.Address;
import com.project.service.user.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AddressService {
    Optional<Address> findById(String id);
    Address getById(String id);
    Address create(AddressCreateDto addressCreateDto);
    void deleteById(String id);
    void update(String id, AddressUpdateDto addressUpdateDto);
    boolean existsById(String id);
    AddressPageDto list(AddressFilterDto filter);

}
