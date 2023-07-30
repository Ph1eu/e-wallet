package com.project.Unit.Controller;

import com.project.Configuration.jwt.JwtServices;
import com.project.Controller.LoginController;
import com.project.Model.User;
import com.project.Payload.Request.AuthenticationRequest.LoginRequest;
import com.project.Service.CustomUserDetail;
import com.project.Service.RoleService;
import com.project.Service.UserDetailServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginControllerTest {
    @Mock
    private JwtServices jwtServices;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailServiceImpl userDetailService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private LoginController loginController;
    @Test
    void authenticateUser() {

    }

    @Test
    void registerUser() {
    }
}