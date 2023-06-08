package com.project.Model;

import java.util.*;

import jakarta.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
@Entity
@Table(name = "users")
public class User{
    @Id
    @Column(name = "id_email",unique = true)
    private String idemail;

    @Column(name = "username",unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "registration_date")
    private Date registration_date;
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "roles",referencedColumnName = "roles_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Role roles ;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "address",referencedColumnName = "address_id")

    private Address address ;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Paymentcard> paymentcards ;
    public User(){

    }
    public User(String id_email, String username, String password, Date registration_date, Role roles) {
        this.idemail = id_email;
        this.username = username;
        this.password = password;
        this.registration_date = registration_date;
        this.roles = roles;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public List<Paymentcard> getPaymentcards() {
        return paymentcards;
    }

    public void setPaymentcards(List<Paymentcard> paymentcards) {
        this.paymentcards = paymentcards;
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