package com.user_management.Controller;

import java.util.*;

import com.user_management.Payload.Request.LoginRequest;
import com.user_management.Payload.Response.UserInforResponse;
import com.user_management.Repository.RoleRepository;
import com.user_management.Repository.UserRepository;
import com.user_management.Service.CustomUserDetail;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController{
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();



        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE)
                .body(new UserInforResponse(userDetails.getEmail(),
                        userDetails.getUsername(), userDetails.getRole())
                     );
    }
}