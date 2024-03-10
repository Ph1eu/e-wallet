package com.project.controller;

import com.project.assembler.UserResourceAssembler;
import com.project.configuration.jwt.JwtServices;
import com.project.service.role.dto.ERole;
import com.project.service.role.dto.RoleDTO;
import com.project.service.user.dto.UserDto;
import com.project.payload.request.CRUDUserInforRequest.RoleCRUD;
import com.project.payload.response.ResponseEntityWrapper;
import com.project.service_impl.role.RoleServiceImpl;
import com.project.service_impl.user.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(JwtServices.class);
    @Autowired
    UserServiceImpl userDetailService;
    @Autowired
    RoleServiceImpl roleServiceImpl;
    @Autowired
    UserResourceAssembler userResourceAssembler;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestParam(value = "email", required = false) String email,
                                         @RequestParam(value = "balance", required = false) Integer balance,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Page<UserDto> users= null;

        PageRequest pageable = PageRequest.of(page, size);

        if (email == null && balance == null) {
            // Case: No email or balance specified
            users = userDetailService.getAllUsersWithBalanceInformation(pageable);
        } else {
            // Other cases where at least one parameter is specified
            if (email != null && balance != null) {
                // Case: Both email and balance specified
                users = userDetailService.getAllUsersWithEmailandBalance(email, balance, pageable);
            } else if (email != null) {
                // Case: Only email specified
                users = userDetailService.getAllUsersWithEmail(email, pageable);
//            } else {
//                // Case: Only balance specified
//                users = userDetailService.getAllUsersWithBalance(balance, pageable);
            }
        }

        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(userResourceAssembler.toCollectionModelInPagedWrapper(users));
        }

    }

    @PostMapping("/role")
    public ResponseEntity<?> addRole(@RequestBody RoleCRUD roleCRUD) {
        if (roleCRUD.getRole() == null || roleCRUD.getRole().equals("user")) {
            RoleDTO roleDTO = new RoleDTO(UUID.randomUUID().toString(), ERole.ROLE_USER);
            if (!roleServiceImpl.checkExistRole(roleDTO)) {
                roleServiceImpl.addRole(roleDTO);
            } else {
                ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("Role existed");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseEntityWrapper);
            }
        } else if (roleCRUD.getRole().equals("admin")) {
            RoleDTO roleDTO = new RoleDTO(UUID.randomUUID().toString(), ERole.ROLE_ADMIN);
            if (!roleServiceImpl.checkExistRole(roleDTO)) {
                roleServiceImpl.addRole(roleDTO);
            } else {
                ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("Role existed");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseEntityWrapper);
            }
        } else {
            ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("Role not allowed");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseEntityWrapper);
        }
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("Successfully added role");
        return ResponseEntity.ok().body(responseEntityWrapper);
    }

}




