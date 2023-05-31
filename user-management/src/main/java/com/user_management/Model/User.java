package com.user_management.Model;

import java.util.*;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
@Entity
@Table(name = "users")
public class User{
    @Id
    @Column(name = "id_email")
    private String idemail;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "registration_date")
    private Date registration_date;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "roles",referencedColumnName = "roles_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Role roles ;

    public User(){

    }
    public User(String id_email, String username, String password, Date registration_date, Role roles) {
        this.idemail = id_email;
        this.username = username;
        this.password = password;
        this.registration_date = registration_date;
        this.roles = roles;
    }

    public String getId_email() {
        return this.idemail;
    }

    public void setId_email(String id_email) {
        this.idemail = id_email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }

    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id_email='" + idemail + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registration_date=" + registration_date +
                ", roles=" + roles +
                '}';
    }
}