package com.project.api.resource.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank
    @JsonProperty("username")
    private String username;
    @NotBlank
    @JsonProperty("password")
    private String password;
    @NotBlank
    @JsonProperty("first_name")
    private String first_name;
    @NotBlank
    @JsonProperty("last_name")
    private String last_name;
    @NotBlank
    @JsonProperty("email")
    private String email;
    @NotBlank
    @JsonProperty("phone")
    private String phone;
    @NotBlank
    @JsonProperty("role")
    private String role;
    @JsonProperty("sign_up_key")
    private String signUpKey;
}
