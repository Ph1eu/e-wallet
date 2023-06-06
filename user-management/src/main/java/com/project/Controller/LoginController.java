package com.project.Controller;

import java.util.*;

import com.project.Configuration.jwt.JwtServices;
import com.project.Model.ERole;
import com.project.Model.Role;
import com.project.Model.User;
import com.project.Payload.Request.AuthenticationRequest.LoginRequest;
import com.project.Payload.Request.AuthenticationRequest.SignUpRequest;
import com.project.Payload.Response.MessageResponse;
import com.project.Payload.Response.UserInforResponse;
import com.project.Repository.RoleRepository;
import com.project.Repository.UserRepository;
import com.project.Service.CustomUserDetail;
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
	@Value("${ph1eu.appprop.signupkey}")
	private String signUpKey;
	
	@Autowired
    JwtServices jwtServices;
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

		String jwtToken = jwtServices.generateToken(userDetails);

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,"Bearer "+jwtToken)
                .body(new UserInforResponse(userDetails.getEmail(),
                        userDetails.getUsername(), userDetails.getRole())
                     );
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByIdemail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword())
                ,new Date(),null);

        String strRoles = signUpRequest.getRole();
        String signUpkey = signUpRequest.getSignUpKey();
        if (strRoles == null || strRoles == "user") {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            user.setRoles(userRole);
        } else if(strRoles== "admin" && signUpkey == this.signUpKey ){

                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        user.setRoles(adminRole);

            }


        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
}
//    @PostMapping("/signout")
////    public ResponseEntity<?> logoutUser() {
////      ResponseCookie cookie = jwtServices.getCleanJwtCookie();
////      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
////          .body(new MessageResponse("You've been signed out!"));
////    }
}