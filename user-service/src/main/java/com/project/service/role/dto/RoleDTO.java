package com.project.service.role.dto;

import com.project.service.role.entity.Role;
import lombok.Data;

@Data
public class RoleDTO {
    private String id;
    private ERole name;
    public RoleDTO(ERole role) {
        this.name = role;
    }

    public RoleDTO(String id, ERole name) {
        this.id = id;
        this.name = name;
    }
    public RoleDTO(Role role) {
        this.id = role.getId();


        this.name = role.getName();
    }
}
