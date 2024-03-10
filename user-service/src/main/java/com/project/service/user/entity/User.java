package com.project.service.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.service.role.entity.Role;
import com.project.service.user.dto.UserDto;
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
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "roles", referencedColumnName = "roles_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Setter
    private Role roles;
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

    public User(UserDto userDTO) {
        this.id = userDTO.getId();
        this.email = userDTO.getEmail();
        this.username = userDTO.getUsername();
        this.password = userDTO.getPassword();
        this.first_name = userDTO.getFirst_name();
        this.last_name = userDTO.getLast_name();
        this.registration_date = userDTO.getRegistration_date();
        if (userDTO.getRoles() == null) {
            this.roles = null;
        } else {
            this.roles = new Role(userDTO.getRoles());
        }
        if (userDTO.getAddressDTO() == null) {
            this.address = null;
        } else {
            this.address = new Address(userDTO.getAddressDTO());
        }
        this.paymentcards = null;


    }

    public User(String email, String username, String password, String first_name, String last_name, Date registration_date, Role roles, Address address, List<Paymentcard> paymentcards) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.registration_date = registration_date;
        this.roles = roles;
        this.address = address;
        this.paymentcards = paymentcards;
    }

}