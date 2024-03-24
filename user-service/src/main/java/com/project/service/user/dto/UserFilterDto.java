package com.project.service.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


public record UserFilterDto(String email, String username, int page, int size) {
}
