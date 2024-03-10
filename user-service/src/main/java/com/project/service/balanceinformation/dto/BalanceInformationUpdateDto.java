package com.project.service.balanceinformation.dto;

import lombok.Data;

@Data
public class BalanceInformationUpdateDto {
    private int balance_amount;
    private String phone_number;
}
