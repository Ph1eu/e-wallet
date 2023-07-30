package com.project.Model;

import com.project.Payload.DTO.RoleDTO;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="roles_id")
    private String id;
    @Enumerated(EnumType.STRING)
    @Column(name="name",length = 20)
    private ERole name;

    public Role(ERole role) {
        this.name = role;
    }
    public Role(){

    }
    public Role(RoleDTO roleDTO){
        this.id = roleDTO.getId();
        this.name = roleDTO.getName();
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
        Role role = (Role) o;
        return Objects.equals(id, role.id) && name == role.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
