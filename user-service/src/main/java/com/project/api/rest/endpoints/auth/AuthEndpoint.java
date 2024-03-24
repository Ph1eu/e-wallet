package com.project.api.rest.endpoints.auth;

import com.project.api.resource.auth.AuthenticationResource;
import com.project.api.resource.auth.request.AuthenticationRequestDto;
import com.project.api.resource.auth.request.RegisterRequestDto;
import com.project.api.resource.auth.respond.AuthenticationRespondDto;
import com.project.api.rest.security.CustomUserDetail;
import com.project.api.rest.security.jwt.JwtServices;
import com.project.api.common.model.ResponseEntityWrapper;
import com.project.service.user.dto.UserDto;
import com.project.service.user.mapper.UserMapper;
import com.project.service_impl.user.UserServiceImpl;
import jakarta.persistence.EntityExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthEndpoint implements AuthenticationResource {
    private final Logger logger = LoggerFactory.getLogger(AuthEndpoint.class);
    @Autowired
    private JwtServices jwtServices;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserMapper userMapper;

    @Value("${ph1eu.appprop.signupkey}")
    private String signUpKey;


    @Override
    public ResponseEntityWrapper<AuthenticationRespondDto> authenticate(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

        String jwtToken = jwtServices.generateToken(userDetails);
        AuthenticationRespondDto authenticationRespondDto = new AuthenticationRespondDto(jwtToken);
        return ResponseEntityWrapper.<AuthenticationRespondDto>builder().message("Login success").status(HttpStatus.ACCEPTED).data(authenticationRespondDto).build();
    }
    @Override
    public ResponseEntityWrapper<AuthenticationRespondDto> register(RegisterRequestDto registerRequestDto) {
        if (userService.existsByUsername(registerRequestDto.getUsername())) {
            throw new EntityExistsException("Username is already taken");
        }
        if(userService.existsByEmail(registerRequestDto.getEmail())) {
            throw new EntityExistsException("Email is already taken");
        }
        if(registerRequestDto.getSignUpKey().equals(signUpKey) && Objects.equals(registerRequestDto.getRole(), "ADMIN")) {
            UserDto userDto =  userMapper.RegisterRequestToUserDto(registerRequestDto);
            userService.create(userDto);
            return ResponseEntityWrapper.<AuthenticationRespondDto>builder().message("Register success").status(HttpStatus.CREATED).build();
        } else if (!registerRequestDto.getSignUpKey().equals(signUpKey) && Objects.equals(registerRequestDto.getRole(), "ADMIN")) {
            return ResponseEntityWrapper.<AuthenticationRespondDto>builder().message("lack verification of admin registering").status(HttpStatus.BAD_REQUEST).build();
        }else{
            UserDto userDto =  userMapper.RegisterRequestToUserDto(registerRequestDto);
            userService.create(userDto);
            return ResponseEntityWrapper.<AuthenticationRespondDto>builder().message("Register success").status(HttpStatus.CREATED).build();
        }

    }
}
