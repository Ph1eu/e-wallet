package com.project.service.role.dto;

import com.project.service.role.entity.Role;
import lombok.Data;

@Data
public class RoleDto {
    private String id;
    private ERole name;
    public RoleDto(ERole role) {
        this.name = role;
    }

    public RoleDto(String id, ERole name) {
        this.id = id;
        this.name = name;
    }
    public RoleDto(Role role) {
        this.id = role.getId();


        this.name = role.getName();
    }
}
