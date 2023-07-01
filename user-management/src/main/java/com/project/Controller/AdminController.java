package com.project.Controller;

import com.project.Assembler.UserResourceAssembler;
import com.project.Configuration.jwt.JwtServices;
import com.project.Model.User;
import com.project.Payload.DTO.UserDTO;
import com.project.Repository.UserRepository;
import com.project.Service.CustomUserDetail;
import com.project.Service.UserDetailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    UserResourceAssembler userResourceAssembler;
    private static final Logger logger = LoggerFactory.getLogger(JwtServices.class);


    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "email",required = false) String email,
                                         @RequestParam(value = "balance",required = false) Integer balance,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size){
        Page<UserDTO> users;

        PageRequest pageable = PageRequest.of(page, size);

        if (email == null && balance == null) {
            // Case: No email or balance specified
            users = userDetailService.getAllUsersWithBalanceInformation(pageable);
        } else {
            // Other cases where at least one parameter is specified
            if (email != null && balance != null) {
                // Case: Both email and balance specified
                users = userDetailService.getAllUsersWithEmailandBalance(email, balance,pageable);
            } else if (email != null) {
                // Case: Only email specified
                users = userDetailService.getAllUsersWithEmail(email,pageable);
            } else {
                // Case: Only balance specified
                users = userDetailService.getAllUsersWithBalance(balance,pageable);
            }
        }

        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(userResourceAssembler.toCollectionModelInPagedWrapper(users));
        }

    }
}




