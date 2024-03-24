package com.project.service.paymentcard;

import com.project.service.paymentcard.dto.*;
import com.project.service.paymentcard.entity.Paymentcard;

import java.util.List;
import java.util.Optional;

public interface PaymentCardService {
    PaymentCardPageDto list(PaymentCardFilterDto paymentCardFilterDto);
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
