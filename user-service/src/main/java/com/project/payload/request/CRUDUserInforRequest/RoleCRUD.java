package com.project.payload.request.CRUDUserInforRequest;

import jakarta.validation.constraints.NotBlank;

public class RoleCRUD {
    @NotBlank
    String role;

    public RoleCRUD() {
    }

    public RoleCRUD(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
