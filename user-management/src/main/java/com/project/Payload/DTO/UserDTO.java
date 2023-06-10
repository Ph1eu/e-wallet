package com.project.Payload.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.Model.Address;
import com.project.Model.Paymentcard;
import com.project.Model.Role;
import com.project.Model.User;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

public class UserDTO {

    private String idemail;
    private String username;
    private String password;
    private Date registration_date;
    private Role roles ;
    private Address address ;
    private List<Paymentcard> paymentcards ;

    public UserDTO() {
    }

    public UserDTO(String idemail, String username, String password, Date registration_date, Role roles, Address address, List<Paymentcard> paymentcards) {
        this.idemail = idemail;
        this.username = username;
        this.password = password;
        this.registration_date = registration_date;
        this.roles = roles;
        this.address = address;
        this.paymentcards = paymentcards;
    }
    public UserDTO(User user){
        this.idemail = user.getId_email();
        this.username= user.getUsername();
        this.password =  user.getPassword();
        this.registration_date = user.getRegistration_date();
        this.roles = user.getRoles();
        this.address = user.getAddress();
        this.paymentcards  = user.getPaymentcards();
    }

    public String getIdemail() {
        return idemail;
    }

    public void setIdemail(String idemail) {
        this.idemail = idemail;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Paymentcard> getPaymentcards() {
        return paymentcards;
    }

    public void setPaymentcards(List<Paymentcard> paymentcards) {
        this.paymentcards = paymentcards;
    }
    public User toUserModel(){
        return new User(this.idemail,this.username,this.password,this.registration_date,this.roles,this.address,this.paymentcards);
    }
}
