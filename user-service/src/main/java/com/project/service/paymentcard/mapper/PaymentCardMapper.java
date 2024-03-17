package com.project.service.paymentcard.mapper;

import com.project.service.paymentcard.dto.PaymentcardDTO;
import com.project.service.paymentcard.entity.Paymentcard;
import org.mapstruct.factory.Mappers;

public interface PaymentCardMapper {
    PaymentCardMapper INSTANCE = Mappers.getMapper(PaymentCardMapper.class);
    PaymentcardDTO paymentCardToPaymentCardDto(Paymentcard paymentCard);
    Paymentcard paymentCardDtoToPaymentCard(PaymentcardDTO paymentCardDto);
}
