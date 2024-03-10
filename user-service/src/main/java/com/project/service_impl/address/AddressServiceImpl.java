package com.project.service_impl.address;

import com.project.service.address.AddressService;
import com.project.service.address.entity.Address;
import com.project.service.user.entity.User;
import com.project.service.address.dto.AddressDto;
import com.project.service.user.dto.UserDto;
import com.project.service_impl.paymentcard.PaymentcardRepository;
import com.project.service_impl.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    private final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PaymentcardRepository paymentcardRepository;
    @Autowired
    private UserRepository userRepository;

    public void deleteAddressByUser(UserDto user) {
        AddressDto address = user.getAddressDTO();
        user.setAddressDTO(null);
        try {

            addressRepository.delete(new Address(address));
            userRepository.save(new User(user));
            logger.info("deleted address for user {}", user.getUsername());
        } catch (Exception e) {
            logger.error("Failed to delete address for user {}", user.getUsername());
            throw new RuntimeException("Failed to delete address.", e);
        }
    }

    public void saveAddressForUser(AddressDto address, UserDto user) {
        try {
            user.setAddressDTO(address);
            userRepository.save(new User(user));
            logger.info("saved address for user {}", user.getUsername());

        } catch (Exception e) {
            logger.error("Failed to save address for user {}", user.getUsername());
            throw new RuntimeException("Failed to save address.", e);
        }
    }
}
