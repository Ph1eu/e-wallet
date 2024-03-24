package com.project.service.user.dto;

import lombok.Data;

public record UserUpdateDto(String email, String username, String password, String first_name, String last_name,
                            String role) {
}
