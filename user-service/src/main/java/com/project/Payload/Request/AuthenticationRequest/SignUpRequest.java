package com.project.Payload.Request.AuthenticationRequest;

import jakarta.validation.constraints.*;
public class SignUpRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String first_name;
    @NotBlank
    private String last_name;
    @Email
    private String email;
    @NotBlank
    private String role;
    private String signUpKey;
    public SignUpRequest(String username, String password,String first_name,String last_name, String email, String role,String signUpKey) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

}
