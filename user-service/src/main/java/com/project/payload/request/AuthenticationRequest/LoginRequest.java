package com.project.payload.request.AuthenticationRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
