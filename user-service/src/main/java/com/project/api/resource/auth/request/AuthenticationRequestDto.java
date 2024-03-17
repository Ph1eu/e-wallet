package com.project.api.resource.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticationRequestDto {
    @NotBlank
    @JsonProperty("username")
    private String username;
    @NotBlank
    @JsonProperty("password")
    private String password;
}
