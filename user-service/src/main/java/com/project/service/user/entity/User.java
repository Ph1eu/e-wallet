package com.project.service.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.service.role.dto.ERole;
import com.project.service.address.entity.Address;
import com.project.service.paymentcard.entity.Paymentcard;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
@EqualsAndHashCode
@ToString
public class User {
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "email", unique = true)
    @Setter
    private String email;
    @Setter
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "password")
    @Setter
    private String password;
    @Column(name = "first_name")
    @Setter
    private String first_name;
    @Column(name = "last_name")
    @Setter
    private String last_name;
    @Column(name = "registration_date")
    @Setter
    private Date registration_date;
    @Setter
    private ERole role;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address", referencedColumnName = "address_id")
    @Setter
    private Address address;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @Setter
    private List<Paymentcard> paymentcards;

    public User() {

    }

}