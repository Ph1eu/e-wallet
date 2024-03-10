package com.project.controller;

import com.project.configuration.jwt.JwtServices;
import com.project.exceptions.custom_exception.BusinessLogic.RoleNotFoundException;
import com.project.exceptions.custom_exception.BusinessLogic.SignUpKeyNotFoundException;
import com.project.exceptions.custom_exception.Database.ExistedInformationException;
import com.project.exceptions.custom_exception.ValidationInput.MissingRequiredFieldException;
import com.project.service.role.dto.ERole;
import com.project.service.role.dto.RoleDTO;
import com.project.service.user.dto.UserDto;
import com.project.payload.request.AuthenticationRequest.LoginRequest;
import com.project.payload.request.AuthenticationRequest.SignUpRequest;
import com.project.payload.response.ResponseEntityWrapper;
import com.project.payload.response.UserInforResponse;
import com.project.service_impl.user.CustomUserDetail;
import com.project.service_impl.role.RoleServiceImpl;
import com.project.service_impl.user.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    JwtServices jwtServices;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserServiceImpl userDetailService;
    @Autowired
    RoleServiceImpl roleServiceImpl;
    @Value("${ph1eu.appprop.signupkey}")
    private String signUpKey;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        handMissingField(bindingResult);

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

        String jwtToken = jwtServices.generateToken(userDetails);
        ResponseEntityWrapper<UserInforResponse> responseEntityWrapper = new ResponseEntityWrapper<>("SIGN IN SUCCESSFULLY");
        responseEntityWrapper.setData(List.of(new UserInforResponse(userDetails.getEmail(),
                userDetails.getUsername(), userDetails.getRole())));
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .body(responseEntityWrapper);

    }

    private void handMissingField(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors: ");
            for (FieldError error : bindingResult.getFieldErrors()) {
                String fieldName = error.getField();
                String errorMessageForField = error.getDefaultMessage();
                errorMessage.append(fieldName).append(" - ").append(errorMessageForField).append(";");
            }
            throw new MissingRequiredFieldException(errorMessage.toString());

        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest, BindingResult bindingResult) {
        handMissingField(bindingResult);
        if (userDetailService.existByUsername(signUpRequest.getUsername())) {
            throw new ExistedInformationException("Username is existed");
        }

        if (userDetailService.existByIdemail(signUpRequest.getEmail())) {
            throw new ExistedInformationException("Email is already in use");
        }
        if (signUpRequest.getRole().equalsIgnoreCase("admin") && signUpRequest.getSignUpKey() == null) {
            logger.error("missing sign up key");
            throw new SignUpKeyNotFoundException("Missing Sign Up Key");
        }

        // Create new user's account
        UserDto user = new UserDto(signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFirst_name(), signUpRequest.getLast_name(), new Date(), null, null, null, null);

        String strRoles = signUpRequest.getRole();
        String signUpkey = signUpRequest.getSignUpKey();
        if (strRoles == null || strRoles.equals("user")) {

            RoleDTO userRole = roleServiceImpl.findbyName(ERole.ROLE_USER);
            System.out.println(userRole);
            if (userRole == null) {
                logger.error("can find role user");
                throw new RoleNotFoundException("Role not found: ROLE_USER");

            }
            user.setRoles(userRole);
        } else if (strRoles.equals("admin") && Objects.equals(signUpkey, this.signUpKey)) {

            RoleDTO adminRole = roleServiceImpl.findbyName(ERole.ROLE_ADMIN);
            if (adminRole == null) {
                logger.error("can find role admin");
                throw new RoleNotFoundException("Role not found: ROLE_ADMIN");
            }
            user.setRoles(adminRole);
        }

        try {
            userDetailService.saveUser(user);
            ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("REGISTRATION SUCCESSFULLY");
            return ResponseEntity.ok(responseEntityWrapper);
        } catch (Exception ex) {
            ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>();
            responseEntityWrapper.setMessage("AN ERROR OCCURRED DURING USER REGISTRATION");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseEntityWrapper);
        }
    }
//    @PostMapping("/signout")
////    public ResponseEntity<?> logoutUser() {
////      ResponseCookie cookie = jwtServices.getCleanJwtCookie();
////      return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
////          .body(new MessageResponse("You've been signed out!"));
////    }
}