package com.project.service.address;

import com.project.service.user.dto.UserDto;
import com.project.service.address.dto.AddressDto;


public interface AddressService {

    void deleteAddressByUser(UserDto user);
    void saveAddressForUser(AddressDto address, UserDto user);

}
