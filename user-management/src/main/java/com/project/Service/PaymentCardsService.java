package com.project.Service;

import com.project.Model.Paymentcard;
import com.project.Model.User;
import com.project.Repository.PaymentcardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
@Transactional

public class PaymentCardsService {
    @Autowired
    PaymentcardRepository paymentcardRepository;
    private final Logger logger = LoggerFactory.getLogger(PaymentCardsService.class);

    public List<Paymentcard> getAllCards(){
        try{
            List<Paymentcard> paymentcards = paymentcardRepository.findAll();
            logger.info("Retrieved {} cards from the database.", paymentcards.size());
            return paymentcards;
        }
        catch(Exception e)
        {
            logger.error("Failed to fetch all cards from the database.", e);
            throw new RuntimeException("Failed to fetch all cards.", e);
        }
    }
    public List<Paymentcard> findByAllUser(User user){
        try{
            List<Paymentcard> paymentcards = paymentcardRepository.findAllByUser(user);
            logger.info("Retrieved {} cards from the database.", paymentcards.size());
            return paymentcards;
        }
        catch(Exception e)
        {
            logger.error("Failed to fetch all cards from username {} the database.",user.getUsername(), e);
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
    public void deleteAllByUser(User user){
        try{
            paymentcardRepository.deleteAllByUser(user);
            logger.info("deleted all cards from user {} in the database.", user.getUsername());

        }
        catch(Exception e)
        {
            logger.error("Failed to delete all cards from user {} in the database.",user.getUsername(), e);
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
    public void saveAllByCards(List<Paymentcard> paymentcardList){
        try{
            paymentcardRepository.saveAll(paymentcardList);
            logger.info("saved all {} cards in the database",paymentcardList.size() );

        }
        catch(Exception e)
        {
            logger.error("fail to save all {} card  in the database",paymentcardList.size(), e);
            throw new RuntimeException("Failed to save all cards.", e);
        }

    }

}
