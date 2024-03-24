package com.project.api.resource.user.request;

import lombok.Data;

import java.util.Date;

@Data
public class UserCreateRequestDto {
    private String email;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private Date registration_date;
    private String role;
}
