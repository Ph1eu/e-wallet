package com.project.service.paymentcard.mapper;

import com.project.service.paymentcard.dto.PaymentCardCreateDto;
import com.project.service.paymentcard.dto.PaymentCardUpdateDto;
import com.project.service.paymentcard.dto.PaymentcardDto;
import com.project.service.paymentcard.entity.Paymentcard;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
@Mapper()
public interface PaymentCardMapper {
    PaymentCardMapper INSTANCE = Mappers.getMapper(PaymentCardMapper.class);
    @Named("paymentCardToPaymentCardDto")
    PaymentcardDto paymentCardToPaymentCardDto(Paymentcard paymentCard);
    @Named("paymentCardDtoToPaymentCard")
    Paymentcard paymentCardDtoToPaymentCard(PaymentcardDto paymentCardDto);
    @Named("paymentCardCreateDtoToPaymentCard")
    Paymentcard paymentCardCreateDtoToPaymentCard(PaymentCardCreateDto paymentCardCreateDto);
    @Named("paymentCardUpdateDtoToPaymentCard")
    Paymentcard paymentCardUpdateDtoToPaymentCard(PaymentCardUpdateDto paymentCardUpdateDto);
}
