package com.project.api.rest.endpoints.login;

import com.project.api.rest.security.jwt.JwtServices;
import com.project.controller.LoginController;
import com.project.payload.response.ResponseEntityWrapper;
import com.project.service_impl.role.RoleServiceImpl;
import com.project.service_impl.user.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginEndpoint {
    private final Logger logger = LoggerFactory.getLogger(LoginEndpoint.class);
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
    public ResponseEntityWrapper<?> authenticateUser() {
        return null;

    }
}
