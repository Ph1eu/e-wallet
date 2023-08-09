package com.project.Payload.Request;

public class BalanceCRUD {
    private String username;
    private Integer balance_amount;
    private String phone_number;

    public BalanceCRUD(String username, Integer balance_amount, String phone_number) {
        this.username = username;
        this.balance_amount = balance_amount;
        this.phone_number = phone_number;
    }

    public BalanceCRUD() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(Integer balance_amount) {
        this.balance_amount = balance_amount;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
