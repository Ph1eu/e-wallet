package com.project.payload.dto;

import com.project.model.BalanceInformation;

import java.util.Objects;


public class BalanceInformationDTO {

    private String id;
    private String userid;

    private int balance_amount;

    private String phone_number;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return userid;
    }

    public void setUser(String user) {
        this.userid = user;
    }

    public int getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(int balance_amount) {
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
        this.userid = balanceInformation.getUser().getId_email();
    }

    public BalanceInformationDTO() {
    }

    public BalanceInformationDTO(String id, String userid, int balance_amount, String phone_number) {
        this.id = id;
        this.userid = userid;
        this.balance_amount = balance_amount;
        this.phone_number = phone_number;
    }

    public BalanceInformationDTO(String user, int balance_amount, String phone_number) {
        this.userid = user;
        this.balance_amount = balance_amount;
        this.phone_number = phone_number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceInformationDTO that = (BalanceInformationDTO) o;
        return balance_amount == that.balance_amount && Objects.equals(phone_number, that.phone_number) && Objects.equals(id, that.id) && Objects.equals(userid, that.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userid, balance_amount, phone_number);
    }
}
