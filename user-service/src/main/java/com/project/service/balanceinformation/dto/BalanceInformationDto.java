package com.project.service.balanceinformation.dto;

import com.project.service.balanceinformation.entity.BalanceInformation;
import lombok.Data;

@Data
public class BalanceInformationDto {

    private String id;
    private String userid;

    private int balance_amount;

    private String phone_number;

    public BalanceInformationDto(BalanceInformation balanceInformation) {
        this.id = balanceInformation.getId();
        this.balance_amount = balanceInformation.getBalance_amount();
        this.phone_number = balanceInformation.getPhone_number();
        this.userid = balanceInformation.getUser();
    }
    public BalanceInformationDto(String id, String userid, int balance_amount, String phone_number) {
        this.id = id;
        this.userid = userid;
        this.balance_amount = balance_amount;
        this.phone_number = phone_number;
    }

}
