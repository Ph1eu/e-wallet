package com.project.service.role.entity;

import com.project.service.role.dto.ERole;
import com.project.service.role.dto.RoleDto;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
    public Role(RoleDto roleDTO) {
        this.id = roleDTO.getId();
        this.name = roleDTO.getName();
    }

    public void setId(String id) {
        this.id = id;
    }


}
