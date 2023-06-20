package com.project.Payload.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.Model.BalanceInformation;
import com.project.Model.User;
import jakarta.persistence.*;

import java.util.Objects;


public class BalanceInformationDTO {

    private String id;
    private UserDTO user;

    private long balance_amount;

    private String phone_number;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public long getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(long balance_amount) {
        this.balance_amount = balance_amount;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public BalanceInformationDTO(BalanceInformation balanceInformation) {
        this.id = balanceInformation.getId();
        this.balance_amount = balanceInformation.getBalance_amount();
        this.phone_number = balanceInformation.getPhone_number();
    }

    public BalanceInformationDTO(UserDTO user, long balance_amount, String phone_number) {
        this.user = user;
        this.balance_amount = balance_amount;
        this.phone_number = phone_number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceInformationDTO that = (BalanceInformationDTO) o;
        return balance_amount == that.balance_amount && phone_number == that.phone_number && Objects.equals(id, that.id) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, balance_amount, phone_number);
    }
}
