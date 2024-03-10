package com.project.payload.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class UserDTO {

    private String idemail;


    private String username;


    private String password;
    private String roles;
    private String first_name;
    private String last_name;
    private String address;
    private Date registration_date;


    @JsonIgnore

    private List<String> paymentcardsDTO;

    public UserDTO() {

    }

    public UserDTO(User user) {
        this.idemail = user.getId_email();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.first_name = user.getFirst_name();
        this.last_name = user.getLast_name();
        this.address = user.getAddress();
        this.registration_date = user.getRegistration_date();
        if (user.getPaymentcards() == null) {
            this.paymentcardsDTO = new ArrayList<>();
        } else {
            this.paymentcardsDTO.addAll(user.getPaymentcards());
        }

    }

    public UserDTO(String idemail, String username, String password, String first_name, String last_name, Date registration_date, List<String> paymentcards, String roles) {
        this.idemail = idemail;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.registration_date = registration_date;
        this.paymentcardsDTO = paymentcards;
        this.roles = roles;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
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


    public List<String> getPaymentcards() {
        return this.paymentcardsDTO;
    }

    public void setPaymentcards(List<String> paymentcards) {
        this.paymentcardsDTO = paymentcards;
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

    public List<String> getPaymentcardsDTO() {
        return paymentcardsDTO;
    }

    public void setPaymentcardsDTO(List<String> paymentcardsDTO) {
        this.paymentcardsDTO = paymentcardsDTO;
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
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(idemail, userDTO.idemail)
                && Objects.equals(username, userDTO.username)
                && Objects.equals(password, userDTO.password)
                && Objects.equals(first_name, userDTO.first_name)
                && Objects.equals(last_name, userDTO.last_name)
                && Objects.equals(registration_date, userDTO.registration_date)

                && Objects.equals(paymentcardsDTO, userDTO.paymentcardsDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idemail, username, password, first_name, last_name, registration_date, paymentcardsDTO);
    }
}