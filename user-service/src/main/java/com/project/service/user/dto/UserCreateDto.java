package com.project.service.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.service.address.dto.AddressDto;
import com.project.service.balanceinformation.dto.BalanceInformationDto;
import com.project.service.paymentcard.dto.PaymentcardDTO;
import com.project.service.role.dto.RoleDTO;
import com.project.service.user.entity.User;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserCreateDto {
    private String id;
    private String email;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private Date registration_date;
    private RoleDTO roles;
}
