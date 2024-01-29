package com.project.service;

import com.project.model.Paymentcard;
import com.project.model.User;
import com.project.payload.dto.PaymentcardDTO;
import com.project.payload.dto.UserDTO;
import com.project.repository.PaymentcardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional

public class PaymentCardsService {
    @Autowired
    PaymentcardRepository paymentcardRepository;
    private final Logger logger = LoggerFactory.getLogger(PaymentCardsService.class);

    public List<PaymentcardDTO> getAllCards(){
        try{
            List<Paymentcard> paymentcards = paymentcardRepository.findAll();
            List< PaymentcardDTO> PaymentcardsDTO = new ArrayList<>();
            for (Paymentcard paymentcard : paymentcards){
                PaymentcardsDTO.add(new PaymentcardDTO(paymentcard));
            }
            logger.info("Retrieved {} cards from the database.", paymentcards.size());
            return PaymentcardsDTO;
        }
        catch(Exception e)
        {
            logger.error("Failed to fetch all cards from the database.", e);
            throw new RuntimeException("Failed to fetch all cards.", e);
        }
    }
    public List<PaymentcardDTO> findByAllUser(UserDTO userDTO){
        try{
            User user = new User(userDTO);
            List<Paymentcard> paymentcards = paymentcardRepository.findAllByUser(user);
            List< PaymentcardDTO> PaymentcardsDTO = new ArrayList<>();
            if(paymentcards.isEmpty()){
                logger.info("No card in the database.");
                return PaymentcardsDTO;

            }
            else{
                for (Paymentcard paymentcard : paymentcards){
                    PaymentcardsDTO.add(new PaymentcardDTO(paymentcard));
                }
                logger.info("Retrieved {} cards from the database.", paymentcards.size());
                return PaymentcardsDTO;
            }
        }
        catch(Exception e)
        {
            logger.error("Failed to fetch all cards from username {} the database.",userDTO.getUsername(), e);
            throw new RuntimeException("Failed to fetch all cards for user.", e);
        }

    }
    public Paymentcard findById(String id){
        try{
            Paymentcard paymentcard = paymentcardRepository.getReferenceById(id);
            logger.info("Retrieved card from id {} in the database.", paymentcard.getId());
            return paymentcard;
        }
        catch(Exception e)
        {
            logger.error("Failed to fetch card with id {} the database.",id, e);
            throw new RuntimeException("Failed to fetch card.", e);
        }

    }
    public void deleteAllByUser(UserDTO userDTO){
        try{

            paymentcardRepository.deleteAllByUser(new User(userDTO));
            logger.info("deleted all cards from user {} in the database.", userDTO.getUsername());

        }
        catch(Exception e)
        {
            logger.error("Failed to delete all cards from user {} in the database.",userDTO.getUsername(), e);
            throw new RuntimeException("Failed to fetch card.", e);
        }
    }
    public void deleteByID(String id){
        try{
            paymentcardRepository.deleteById(id);
            logger.info("deleted card with id {} in the database",id );

        }
        catch(Exception e)
        {
            logger.error("fail to delete card with id {} in the database",id, e);
            throw new RuntimeException("Failed to delete card.", e);
        }

    }
    public void saveAllByCards(List<PaymentcardDTO> paymentcardDTOList){
        try{
            List<Paymentcard> paymentcards = new ArrayList<>();
            for (PaymentcardDTO paymentcardDTO: paymentcardDTOList){
                paymentcards.add(new Paymentcard(paymentcardDTO.getId(),paymentcardDTO.getCard_number(),new User(paymentcardDTO.getUser()),
                        paymentcardDTO.getCard_holder_name(),paymentcardDTO.getCard_type(),paymentcardDTO.getRegistration_date()
                ,paymentcardDTO.getExpiration_date()));
            }
            paymentcardRepository.saveAll(paymentcards);
            logger.info("saved all {} cards in the database",paymentcardDTOList.size() );

        }
        catch(Exception e)
        {
            logger.error("fail to save all {} card  in the database",paymentcardDTOList.size(), e);
            throw new RuntimeException("Failed to save all cards.", e);
        }

    }

}
