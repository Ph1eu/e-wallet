package com.project.Payload.DTO;

import com.project.Model.ERole;
import com.project.Model.Role;

import java.util.Objects;


public class RoleDTO {

    private String id;

    private ERole name;

    public RoleDTO(ERole role) {
        this.name = role;
    }
    public RoleDTO(){

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
