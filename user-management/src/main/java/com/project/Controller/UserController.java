package com.project.Controller;

import com.project.Model.Address;
import com.project.Model.User;
import com.project.Payload.Request.CRUDUserInforRequest.AddressCRUD;
import com.project.Payload.Response.MessageResponse;
import com.project.Repository.AddressRepository;
import com.project.Repository.PaymentcardRepository;
import com.project.Repository.UserRepository;
import com.project.Service.UserDetailServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailServiceImpl userDetailService;
    @PostMapping("/address")
    public ResponseEntity<?> changeAddress(@Valid @RequestBody AddressCRUD addressCRUD){

        if (!userRepository.existsByIdemail(addressCRUD.getId_email())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User doesn't exists!"));
        }
        User user = userRepository.getReferenceById(addressCRUD.getId_email());
        user.setAddress(new Address(addressCRUD.getStreet_address(),
                addressCRUD.getCity(),addressCRUD.getProvince(),addressCRUD.getCountry()
                ));
        userRepository.save(user);
        return ResponseEntity.ok().body(new MessageResponse("User address has been changed"));
    }
}
