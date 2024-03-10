package com.project.service.paymentcard;

import com.project.service.user.dto.UserDto;
import com.project.service.paymentcard.dto.PaymentcardDTO;
import com.project.service.paymentcard.entity.Paymentcard;

import java.util.List;
import java.util.Optional;

public interface PaymentCardService {
    List<PaymentcardDTO> getAll();
    List<PaymentcardDTO> getAllByUser(UserDto userDTO);
    Optional<Paymentcard> findById(String id);
    Paymentcard getById(String id);
    void deleteAllByUser(UserDto userDTO);
    void deleteByID(String id);
    void saveAllByCards(List<PaymentcardDTO> paymentcardDTOList);
}
