package com.project.Payload.Request.AuthenticationRequest;

import jakarta.validation.constraints.*;
public class SignUpRequest {

    private String username;

    private String password;
    @Email
    private String email;
    private String role;
    private String signUpKey;
    public SignUpRequest(String username, String password, String email, String role,String signUpKey) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.signUpKey=signUpKey;
        
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

	public String getSignUpKey() {
		return signUpKey;
	}

	public void setSignUpKey(String signUpKey) {
		this.signUpKey = signUpKey;
	}
    
}
