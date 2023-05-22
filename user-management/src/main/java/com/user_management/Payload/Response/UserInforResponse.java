package com.user_management.Payload.Response;

public class UserInforResponse {
    private String id_email;
    private String username;
    private String role;

    public UserInforResponse(String id_email, String username, String role) {
        this.id_email = id_email;
        this.username = username;
        this.role = role;
    }

    public String getId_email() {
        return id_email;
    }

    public void setId_email(String id_email) {
        this.id_email = id_email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
