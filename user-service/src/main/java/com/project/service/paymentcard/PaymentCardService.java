package com.project.service.paymentcard;

import com.project.service.paymentcard.dto.PaymentCardCreateDto;
import com.project.service.paymentcard.dto.PaymentCardFilterDto;
import com.project.service.paymentcard.dto.PaymentCardUpdateDto;
import com.project.service.user.dto.UserDto;
import com.project.service.paymentcard.dto.PaymentcardDto;
import com.project.service.paymentcard.entity.Paymentcard;

import java.util.List;
import java.util.Optional;

public interface PaymentCardService {
    List<PaymentcardDto> list(PaymentCardFilterDto paymentCardFilterDto);
    Optional<Paymentcard> findById(String id);
    Paymentcard getById(String id);
    void deleteAllByUserId(String userid);
    void deleteByID(String id);
    void createByList(List<PaymentcardDto> paymentcardDtoList);
    Paymentcard create(PaymentCardCreateDto paymentCardCreateDto);
    void update(String id, PaymentCardUpdateDto paymentCardUpdateDto);
    boolean existsById(String id);
    boolean existsByCardNumber(String cardNumber);
}
