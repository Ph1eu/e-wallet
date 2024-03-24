package com.project.api.rest.endpoints.card;

import com.project.api.common.model.ResponseEntityWrapper;
import com.project.api.resource.paymentcard.PaymentCardResource;
import com.project.api.resource.paymentcard.model.PaymentCardResourceDto;
import com.project.api.resource.paymentcard.request.PaymentCardFilterRequestDto;
import com.project.api.resource.paymentcard.request.PaymentCardUpdateRequestDto;
import com.project.api.resource.paymentcard.respond.PagePaymentCardRespondDto;
import com.project.service.paymentcard.dto.PaymentCardFilterDto;
import com.project.service.paymentcard.dto.PaymentCardPageDto;
import com.project.service.paymentcard.dto.PaymentCardUpdateDto;
import com.project.service.paymentcard.dto.PaymentcardDto;
import com.project.service.paymentcard.entity.Paymentcard;
import com.project.service.paymentcard.mapper.PaymentCardMapper;
import com.project.service.user.entity.User;
import com.project.service_impl.paymentcard.PaymentCardServiceImpl;
import com.project.service_impl.user.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RequestMapping("api/user/{userId}/cards")
@RestController
public class PaymentCardEndpoint implements PaymentCardResource {
    private final Logger logger = LoggerFactory.getLogger(PaymentCardEndpoint.class);
    @Autowired
    private PaymentCardServiceImpl paymentCardService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PaymentCardMapper paymentCardMapper;
    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional(readOnly = true)
    public ResponseEntityWrapper<PaymentCardResourceDto> getByID(String id, String userId) {
        logger.info("Request to get payment card with id: {}", id);
        PaymentcardDto paymentcardDto = paymentCardMapper.paymentCardToPaymentCardDto(paymentCardService.getById(id));
        logger.debug("Successfully retrieved payment card with id: {}", id);
        return ResponseEntityWrapper.<PaymentCardResourceDto>builder().message("Payment card found").data(paymentCardMapper.paymentCardDtoToPaymentCardResourceDto(paymentcardDto)).build();
    }

    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional
    public ResponseEntityWrapper<PaymentCardResourceDto> create(PaymentCardResourceDto paymentCardResourceDto, String userId) {
        logger.info("Request to create payment card with :{}", paymentCardResourceDto.toString());
        User user = userService.getById(userId);
        PaymentcardDto paymentcardDto = paymentCardMapper.paymentCardResourceDtoToPaymentCardDto(paymentCardResourceDto);
        Paymentcard paymentcard = paymentCardMapper.paymentCardDtoToPaymentCard(paymentcardDto);
        if(user.getPaymentcards() == null) {
            ArrayList<Paymentcard> paymentcards = new ArrayList<>();
            paymentcards.add(paymentcard);
            user.setPaymentcards(paymentcards);
        }
        else {
            user.getPaymentcards().add(paymentcard);
        }
        logger.debug("Successfully created payment card with id: {}", paymentcard.getId());
        return ResponseEntityWrapper.<PaymentCardResourceDto>builder().message("Payment card created").data(paymentCardResourceDto).build();
    }

    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional
    public ResponseEntityWrapper<PaymentCardResourceDto> update(String id, PaymentCardUpdateRequestDto paymentCardUpdateRequestDto, String userId) {
        logger.info("Request to update payment card with id: {}", id);
        PaymentCardUpdateDto paymentCardUpdateDto = paymentCardMapper.paymentCardUpdateRequestDtoToPaymentCardUpdateDto(paymentCardUpdateRequestDto);
        paymentCardService.update(id,paymentCardUpdateDto);
        logger.debug("Successfully updated payment card with id: {}", id);
        return ResponseEntityWrapper.<PaymentCardResourceDto>builder().message("Payment card updated").build();
    }

    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional
    public ResponseEntityWrapper<PaymentCardResourceDto> delete(String id, String userId) {
        logger.info("Request to delete payment card with id: {}", id);
        paymentCardService.deleteByID(id);
        logger.debug("Successfully deleted payment card with id: {}", id);
        return ResponseEntityWrapper.<PaymentCardResourceDto>builder().message("Payment card deleted").build();
    }

    @Override
    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional(readOnly = true)
    public ResponseEntityWrapper<PagePaymentCardRespondDto> list(PaymentCardFilterRequestDto paymentCardFilterDto, String userId) {
        logger.info("Request to list payment cards with filter: {}", paymentCardFilterDto.toString());
        PaymentCardFilterDto paymentCardFilter = paymentCardMapper.paymentCardFilterRequestDtoToPaymentCardFilterDto(paymentCardFilterDto);
        PaymentCardPageDto paymentCardPageDto = paymentCardService.list(paymentCardFilter);
        PagePaymentCardRespondDto pagePaymentCardRespondDto = paymentCardMapper.paymentCardPageDtoToPagePaymentCardRespondDto(paymentCardPageDto);
        logger.debug("Successfully listed payment cards with filter: {}", paymentCardFilterDto.toString());
        return ResponseEntityWrapper.<PagePaymentCardRespondDto>builder().message("Payment cards found").data(pagePaymentCardRespondDto).build();
    }
}
