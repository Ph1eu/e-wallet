package com.project.service.user.dto;

import com.project.service.role.dto.RoleDTO;
import lombok.Data;

import java.util.Date;
@Data
public class UserUpdateDto {
    private String email;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private Date registration_date;
    private RoleDTO roles;
}
