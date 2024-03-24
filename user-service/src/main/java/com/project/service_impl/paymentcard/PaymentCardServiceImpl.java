package com.project.service_impl.paymentcard;

import com.project.service.paymentcard.PaymentCardService;
import com.project.service.paymentcard.dto.*;
import com.project.service.paymentcard.entity.Paymentcard;
import com.project.service.paymentcard.mapper.PaymentCardMapper;
import com.project.service.user.entity.User;
import com.project.service_impl.user.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentCardServiceImpl implements PaymentCardService {
    private final Logger logger = LoggerFactory.getLogger(PaymentCardServiceImpl.class);
    @Autowired
    private PaymentcardRepository paymentcardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentCardMapper paymentCardMapper;
    @Override
    @Transactional(readOnly = true)
    public PaymentCardPageDto list(PaymentCardFilterDto paymentCardFilterDto) {
        logger.info("Request to get list of payment cards by filter: {}", paymentCardFilterDto);
        Specification<Paymentcard> specification = Specification.where(PaymentCardSpecification.hasCardType(paymentCardFilterDto.card_type()));
        Pageable pageable = PageRequest.of(paymentCardFilterDto.page(), paymentCardFilterDto.size());
        List<Paymentcard> paymentcardList = paymentcardRepository.findAll(specification, pageable).toList();
        List<PaymentcardDto> paymentcardDtoList = paymentcardList.stream().map(paymentCardMapper::paymentCardToPaymentCardDto).toList();
        PaymentCardPageDto paymentCardPageDto = new PaymentCardPageDto(paymentCardFilterDto.page(), paymentCardFilterDto.size(), paymentcardDtoList);
        logger.debug("Successfully got list of payment cards by filter: {}", paymentCardFilterDto);
        return paymentCardPageDto;

    }



    @Override
    @Transactional(readOnly = true)
    public Optional<Paymentcard> findById(String id) {
        logger.info("Request to find payment card by id: {}", id);
        Optional<Paymentcard> paymentcard = paymentcardRepository.findById(id);
        logger.debug("Successfully Found payment card: {}", paymentcard);
        return paymentcard;
    }

    @Override
    @Transactional(readOnly = true)
    public Paymentcard getById(String id) {
        logger.info("Request to get payment card by id: {}", id);
        Paymentcard paymentcard = findById(id).orElseThrow(() -> new EntityNotFoundException("Payment card not found"));
        logger.debug("Successfully got payment card: {}", paymentcard);
        return paymentcard;
    }

    @Override
    @Transactional
    public void deleteAllByUserId(String userid) {
        logger.info("Request to delete all payment cards by user id: {}", userid);
        Optional<User> user = userRepository.findById(userid);
        if(user.isEmpty()){
            throw new EntityNotFoundException("User not found by id: "+userid);
        }
        List<Paymentcard>  paymentcardList = user.get().getPaymentcards();
        paymentcardRepository.deleteAll(paymentcardList);
        logger.debug("Successfully deleted all payment cards by user id: {}", userid);
    }

    @Override
    @Transactional
    public void deleteByID(String id) {
        logger.info("Request to delete payment card by id: {}", id);
        paymentcardRepository.deleteById(id);
        logger.debug("Successfully deleted payment card by id: {}", id);
    }

    @Override
    public void createByList(List<PaymentcardDto> paymentcardDtoList) {
        logger.info("Request to create payment cards by list: {}", paymentcardDtoList);
        List<Paymentcard> paymentcardList = paymentcardDtoList.stream().map(paymentCardMapper::paymentCardDtoToPaymentCard).toList();
        paymentcardRepository.saveAll(paymentcardList);
        logger.debug("Successfully created payment cards by list: {}", paymentcardList);
    }

    @Override
    @Transactional
    public Paymentcard create(PaymentCardCreateDto paymentCardCreateDto) {
        logger.info("Request to create payment card: {}", paymentCardCreateDto);
        if(existsById(paymentCardCreateDto.id())){
            throw new EntityExistsException("Payment card already exists by id: "+paymentCardCreateDto.id());
        }
        if(existsByCardNumber(paymentCardCreateDto.card_number())){
            throw new EntityExistsException("Payment card already exists by card number: "+paymentCardCreateDto.card_number());
        }
        Paymentcard paymentCard = paymentCardMapper.paymentCardCreateDtoToPaymentCard(paymentCardCreateDto);
        paymentcardRepository.save(paymentCard);
        logger.debug("Successfully created payment card: {}", paymentCard);
        return paymentCard;
    }

    @Override
    @Transactional
    public void update(String id, PaymentCardUpdateDto paymentCardUpdateDto) {
        logger.info("Request to update payment card by id: {}", id);
        if(existsById(id)){
            throw new EntityNotFoundException("Payment card not found by id: "+id);
        }
        Paymentcard paymentCard = paymentCardMapper.paymentCardUpdateDtoToPaymentCard(paymentCardUpdateDto);
        paymentcardRepository.save(paymentCard);
        logger.debug("Successfully updated payment card: {}", paymentCard);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        logger.info("Request to check if payment card exists by id: {}", id);
        boolean exists = paymentcardRepository.existsById(id);
        logger.debug("Payment card exists by id: {}", exists);
        return exists;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCardNumber(String cardNumber) {
        logger.info("Request to check if payment card exists by card number: {}", cardNumber);
        boolean exists = paymentcardRepository.existsByCard_number(cardNumber);
        logger.debug("Payment card exists by card number: {}", exists);
        return exists;
    }
}


