package com.user_management.Model;

import java.util.*;
import org.springframework.data.annotation.*;

public class User{
    @Id
    private String id_email;
    private String username;
    private String password;
    private Date registration_date;

    public User(String id_email, String username, String password, Date registration_date) {
        this.id_email = id_email;
        this.username = username;
        this.password = password;
        this.registration_date = registration_date;
    }

    public String getId_email() {
        return id_email;
    }

    public void setId_email(String id_email) {
        this.id_email = id_email;
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

    @Override
    public String toString() {
        return "User{" +
                "id_email='" + id_email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", registration_date=" + registration_date +
                '}';
    }
}