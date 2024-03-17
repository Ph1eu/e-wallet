package com.project.service.user.dto;

import lombok.Data;

@Data
public class UserFilterDto {
    private String email;
    private String username;
    private int page;
    private int size;
}
