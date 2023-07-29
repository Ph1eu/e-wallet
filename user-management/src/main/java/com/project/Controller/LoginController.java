package com.project.Controller;

import java.util.*;

import com.project.Configuration.jwt.JwtServices;
import com.project.Exceptions.CustomException.BusinessLogic.RoleNotFoundException;
import com.project.Exceptions.CustomException.BusinessLogic.SignUpKeyNotFoundException;
import com.project.Exceptions.CustomException.Database.ExistedInformationException;
import com.project.Exceptions.CustomException.ValidationInput.MissingRequiredFieldException;
import com.project.Model.ERole;
import com.project.Payload.DTO.RoleDTO;
import com.project.Payload.DTO.UserDTO;
import com.project.Payload.Request.AuthenticationRequest.LoginRequest;
import com.project.Payload.Request.AuthenticationRequest.SignUpRequest;
import com.project.Payload.Response.MessageResponse;
import com.project.Payload.Response.ResponseEntityWrapper;
import com.project.Payload.Response.UserInforResponse;
import com.project.Service.CustomUserDetail;
import com.project.Service.RoleService;
import com.project.Service.UserDetailServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    UserDetailServiceImpl userDetailService;
    @Autowired
    RoleService roleService;
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,BindingResult bindingResult) {
        handMissingField(bindingResult);
        try{
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

		String jwtToken = jwtServices.generateToken(userDetails);
        ResponseEntityWrapper<UserInforResponse> responseEntityWrapper = new ResponseEntityWrapper<>("SIGN IN SUCCESSFULLY");
        responseEntityWrapper.setData(List.of(new UserInforResponse(userDetails.getEmail(),
                userDetails.getUsername(), userDetails.getRole())));
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,"Bearer "+jwtToken)
                .body(responseEntityWrapper);
        }
        catch(UsernameNotFoundException usernameNotFoundException){
            ResponseEntityWrapper<UserInforResponse> responseEntityWrapper = new ResponseEntityWrapper<>("USER'S INFORMATION NOT FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(responseEntityWrapper
                    );
        }
    }

    private void handMissingField(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors: ");
            for (FieldError error : bindingResult.getFieldErrors()) {
                String fieldName = error.getField();
                String errorMessageForField = error.getDefaultMessage();
                errorMessage.append(fieldName).append(" - ").append(errorMessageForField).append(";");
            }
            throw  new MissingRequiredFieldException(errorMessage.toString());

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
        if (signUpRequest.getRole().equalsIgnoreCase("admin") && signUpRequest.getSignUpKey() == null){
            logger.error("missing sign up key");
            throw new SignUpKeyNotFoundException("Missing Sign Up Key");
        }

        // Create new user's account
        UserDTO user = new UserDTO(signUpRequest.getEmail(),
                signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFirst_name(),signUpRequest.getLast_name(),new Date(),null,null,null,null);

        String strRoles = signUpRequest.getRole();
        System.out.println(strRoles);
        String signUpkey = signUpRequest.getSignUpKey();
        if (strRoles == null || strRoles.equals("user")) {

            RoleDTO userRole = roleService.findbyName(ERole.ROLE_USER);
            System.out.println(userRole);
            if(userRole == null){
                logger.error("can find role user");
                throw new RoleNotFoundException("Role not found: ROLE_USER");

            }
            user.setRoles(userRole);
        } else if(strRoles.equals("admin") && Objects.equals(signUpkey, this.signUpKey)){

            RoleDTO adminRole = roleService.findbyName(ERole.ROLE_ADMIN);
            if(adminRole == null){
                logger.error("can find role admin");
                throw new RoleNotFoundException("Role not found: ROLE_ADMIN");            }
            user.setRoles(adminRole);
            }

        try{
        userDetailService.saveUser(user);
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("REGISTRATION SUCCESSFULLY");
        return ResponseEntity.ok(responseEntityWrapper);
        }catch (Exception ex) {
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