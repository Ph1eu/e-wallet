package com.project.Unit.Controller;

import com.project.Assembler.UserResourceAssembler;
import com.project.Controller.AdminController;
import com.project.Model.ERole;
import com.project.Payload.DTO.RoleDTO;
import com.project.Payload.DTO.UserDTO;
import com.project.Service.UserDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminControllerTest {
    @InjectMocks
    private AdminController adminController;
    @Mock
    private UserDetailServiceImpl userDetailService;
    @Mock
    private UserResourceAssembler userResourceAssembler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
    }
}
