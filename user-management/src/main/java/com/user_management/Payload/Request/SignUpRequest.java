package com.user_management.Payload.Request;

import jakarta.validation.constraints.*;
public class SignUpRequest {
    @NotBlank
    @Size(min=10,max=20)
    private String username;
    @NotBlank
    @Size(min = 8, max = 40)
    private String password;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    private String role;

    public SignUpRequest(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
