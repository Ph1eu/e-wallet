package com.project.api.resource.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.service.role.dto.ERole;
import lombok.Data;

@Data
public class UserUpdateRequestDto {
    @JsonProperty("email")
    private String email ;
    @JsonProperty("password")
    private String password ;
    @JsonProperty("first_name")
    private String first_name;
    @JsonProperty("last_name")
    private String last_name;
    @JsonProperty("role")
    private String role = ERole.ROLE_USER.name();
}
