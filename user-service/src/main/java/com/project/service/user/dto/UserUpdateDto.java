package com.project.service.user.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String email;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String role;
}
