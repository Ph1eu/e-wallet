package com.project.Model;

import jakarta.persistence.*;

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
}
