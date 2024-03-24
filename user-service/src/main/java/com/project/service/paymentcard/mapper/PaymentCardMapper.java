package com.project.service.paymentcard.mapper;

import com.project.api.resource.paymentcard.model.PaymentCardResourceDto;
import com.project.api.resource.paymentcard.request.PaymentCardFilterRequestDto;
import com.project.api.resource.paymentcard.request.PaymentCardUpdateRequestDto;
import com.project.api.resource.paymentcard.respond.PagePaymentCardRespondDto;
import com.project.service.paymentcard.dto.*;
import com.project.service.paymentcard.entity.Paymentcard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
    @Named("paymentCardDtoToPaymentCardResourceDto")
    PaymentCardResourceDto paymentCardDtoToPaymentCardResourceDto(PaymentcardDto paymentcardDto);
    @Named("paymentCardResourceDtoToPaymentCardDto")
    PaymentcardDto paymentCardResourceDtoToPaymentCardDto(PaymentCardResourceDto paymentCardResourceDto);
    @Named("paymentCardResourceDtoToPaymentCardCreateDto")
    PaymentCardUpdateDto paymentCardUpdateRequestDtoToPaymentCardUpdateDto(PaymentCardUpdateRequestDto paymentCardUpdateRequestDto);
    @Named("paymentCardFilterRequestDtoToPaymentCardFilterDto")
    PaymentCardFilterDto paymentCardFilterRequestDtoToPaymentCardFilterDto(PaymentCardFilterRequestDto paymentCardFilterDto);
    @Mapping(source = "content", target = "content", qualifiedByName = "paymentCardDtoToPaymentCardResourceDto")
    PagePaymentCardRespondDto paymentCardPageDtoToPagePaymentCardRespondDto(PaymentCardPageDto paymentCardPageDto);
}
