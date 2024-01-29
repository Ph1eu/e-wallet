package com.project.service;

import com.project.model.Address;
import com.project.model.User;
import com.project.payload.dto.AddressDTO;
import com.project.payload.dto.UserDTO;
import com.project.repository.AddressRepository;
import com.project.repository.PaymentcardRepository;
import com.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AddressService {
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    PaymentcardRepository paymentcardRepository;
    @Autowired
    UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(AddressService.class);


    public void deleteAddressByUser(UserDTO user){
        AddressDTO address = user.getAddress();
        user.setAddress(null);
        try{

            addressRepository.delete(new Address(address));
            userRepository.save(new User(user));
            logger.info("deleted address for user {}",user.getUsername());
    }catch (Exception e){
            logger.error("Failed to delete address for user {}",user.getUsername());
            throw new RuntimeException("Failed to delete address.", e);
        }
    }
    public void saveAddressForUser(AddressDTO address,UserDTO user){
        try{
            user.setAddress(address);
            userRepository.save(new User(user));
            logger.info("saved address for user {}",user.getUsername());

        }
        catch (Exception e)
        {
            logger.error("Failed to save address for user {}",user.getUsername());
            throw new RuntimeException("Failed to save address.", e);
        }
    }
}
