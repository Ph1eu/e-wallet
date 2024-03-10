package com.project.service.role.entity;

import com.project.service.role.dto.ERole;
import com.project.service.role.dto.RoleDTO;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Entity
@Table(name = "roles")
@EqualsAndHashCode
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "roles_id")
    private String id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 20)
    @Setter
    private ERole name;

    public Role(ERole role) {
        this.name = role;
    }

    public Role() {

    }
    public Role(RoleDTO roleDTO) {
        this.id = roleDTO.getId();
        this.name = roleDTO.getName();
    }

    public void setId(String id) {
        this.id = id;
    }


}
