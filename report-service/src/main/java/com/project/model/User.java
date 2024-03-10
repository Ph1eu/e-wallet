package com.project.model;

import com.project.payload.dto.UserDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Immutable
public class User {
    @Id
    @Column(name = "id_email", unique = true)
    private String idemail;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "registration_date")
    private Date registration_date;
    @Column(name = "address")
    private String address;
    @Column(name = "roles")
    private String roles;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "paymentcards", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "card_id")
    private List<String> paymentcards;

    public User() {

    }

    public User(UserDTO userDTO) {
        this.idemail = userDTO.getId_email();
        this.username = userDTO.getUsername();
        this.password = userDTO.getPassword();
        this.first_name = userDTO.getFirst_name();
        this.last_name = userDTO.getLast_name();
        this.registration_date = userDTO.getRegistration_date();
        this.paymentcards = null;


    }

    public User(String idemail, String username, String password, String first_name, String last_name, Date registration_date, List<String> paymentcards) {
        this.idemail = idemail;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.registration_date = registration_date;
        this.paymentcards = paymentcards;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public void setPaymentcardswithDTO(List<String> paymentcarsdDTO) {
        if (paymentcarsdDTO == null) {
            this.paymentcards = new ArrayList<>();
        } else {
            this.paymentcards.addAll(paymentcarsdDTO);
        }
    }

    public List<String> getPaymentcards() {
        return paymentcards;
    }

    public void setPaymentcards(List<String> paymentcards) {
        this.paymentcards = paymentcards;
    }

    public String getIdemail() {
        return idemail;
    }

    public void setIdemail(String idemail) {
        this.idemail = idemail;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id_email='" + idemail + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registration_date=" + registration_date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(idemail, user.idemail) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(first_name, user.first_name) && Objects.equals(last_name, user.last_name) && Objects.equals(registration_date, user.registration_date) && Objects.equals(paymentcards, user.paymentcards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idemail, username, password, first_name, last_name, registration_date, paymentcards);
    }
}