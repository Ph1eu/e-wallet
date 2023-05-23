package com.user_management.Controller;

import java.util.*;

import com.user_management.Model.ERole;
import com.user_management.Model.Role;
import com.user_management.Model.User;
import com.user_management.Payload.Request.LoginRequest;
import com.user_management.Payload.Request.SignUpRequest;
import com.user_management.Payload.Response.MessageResponse;
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
//    @Autowired
//    AuthenticationManager authenticationManager;
//    @Autowired
//    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
//
//
//
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE)
//                .body(new UserInforResponse(userDetails.getEmail(),
//                        userDetails.getUsername(), userDetails.getRole())
//                     );
//    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByIdemail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                //passwordEncoder.encode(signUpRequest.getPassword())
                signUpRequest.getPassword()
                ,new Date(),null);

        String strRoles = signUpRequest.getRole();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            user.setRoles(userRole);
        } else {
                switch (strRoles) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        user.setRoles(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        user.setRoles(userRole);
                }
            }


        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
}
}