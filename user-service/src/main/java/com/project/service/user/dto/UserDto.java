package com.project.service.user.dto;

import com.project.service.balanceinformation.dto.BalanceInformationDto;
import com.project.service.address.dto.AddressDto;
import com.project.service.paymentcard.dto.PaymentcardDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

public record UserDto(String id, String email, String username, String password, String first_name, String last_name,
                      Date registration_date, String role, AddressDto address, List<PaymentcardDto> paymentcards,
                      BalanceInformationDto balanceInformation) {
}