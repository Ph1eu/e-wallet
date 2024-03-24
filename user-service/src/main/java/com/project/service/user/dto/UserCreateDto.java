package com.project.service.user.dto;

import lombok.Data;

import java.util.Date;

public record UserCreateDto(String id, String email, String username, String password, String first_name,
                            String last_name, Date registration_date, String role) {
}
