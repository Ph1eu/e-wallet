package com.project.service.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.service.user.entity.User;
import com.project.service.balanceinformation.dto.BalanceInformationDto;
import com.project.service.address.dto.AddressDto;
import com.project.service.paymentcard.dto.PaymentcardDTO;
import com.project.service.role.dto.RoleDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String id;
    @JsonIgnore
    private String email;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private Date registration_date;
    private RoleDTO roles;

    @JsonIgnore
    private AddressDto addressDTO;
    @JsonIgnore
    private List<PaymentcardDTO> paymentcardsDTO;
    private BalanceInformationDto balanceInformation;

    public UserDto() {

    }

    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.first_name = user.getFirst_name();
        this.last_name = user.getLast_name();
        this.registration_date = user.getRegistration_date();
        if (user.getRoles() == null) {
            this.roles = null;
        } else {
            this.roles = new RoleDTO(user.getRoles());
        }
        if (user.getAddress() == null) {
            this.addressDTO = null;
        } else {

            this.addressDTO = new AddressDto(user.getAddress().getId(), user.getAddress().getStreet_address(), user.getAddress().getCity(), user.getAddress().getProvince(), user.getAddress().getCountry());
        }
        // add card in controller layer to avoid collision
    }

    public UserDto(String idemail, String username, String password, String first_name, String last_name, Date registration_date, RoleDTO roles, AddressDto addressDTO, List<PaymentcardDTO> paymentcardsDTO, BalanceInformationDto balanceInformation) {
        this.email = idemail;
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.registration_date = registration_date;
        this.roles = roles;
        this.addressDTO = addressDTO;
        this.paymentcardsDTO = paymentcardsDTO;
        this.balanceInformation = balanceInformation;
    }

}