package com.project.payload.dto;

import com.project.model.ERole;
import com.project.model.Role;

import java.util.Objects;


public class RoleDTO {

    private String id;

    private ERole name;

    public RoleDTO(ERole role) {
        this.name = role;
    }
    public RoleDTO(){

    }

    public RoleDTO(String id, ERole name) {
        this.id = id;
        this.name = name;
    }

    public RoleDTO(Role role){
        this.id = role.getId();


        this.name= role.getName();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDTO roleDTO = (RoleDTO) o;
        return Objects.equals(id, roleDTO.id) && name == roleDTO.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
