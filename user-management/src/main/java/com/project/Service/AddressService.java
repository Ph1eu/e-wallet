package com.project.Service;

import com.project.Model.Address;
import com.project.Model.User;
import com.project.Payload.DTO.AddressDTO;
import com.project.Payload.DTO.UserDTO;
import com.project.Repository.AddressRepository;
import com.project.Repository.PaymentcardRepository;
import com.project.Repository.UserRepository;
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
