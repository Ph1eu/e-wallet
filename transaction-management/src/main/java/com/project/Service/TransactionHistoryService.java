package com.project.Service;

import com.project.Model.*;
import com.project.Payload.DTO.TransactionHistoryDTO;
import com.project.Repository.BalanceInformationRepository;
import com.project.Repository.TransactionHistoryRepository;
import com.project.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional

public class TransactionHistoryService {
    @Autowired
    BalanceInformationRepository balanaceInformationRepository;
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(TransactionHistoryService.class);

    public void saveTransaction( TransactionHistoryDTO transactionHistoryDTO){
        try{
        User sender = userRepository.getReferenceById(transactionHistoryDTO.getSenderid());
        User recipient = userRepository.getReferenceById(transactionHistoryDTO.getRecipientid());
        TransactionHistory transactionHistory = new TransactionHistory(transactionHistoryDTO);
        transactionHistory.setSender(sender);
        transactionHistory.setRecipient(recipient);
        transactionHistoryRepository.save(transactionHistory);
        logger.info("successfully save transaction {}",transactionHistory.getId());
        }catch (Exception e){
            logger.error("Failed to save transaction", e);
            throw new RuntimeException("Failed to save transaction", e);
        }
    }
    public List<TransactionHistoryDTO> getAllTransactionHistory(){
        try{
            List<TransactionHistory> transactionHistory = transactionHistoryRepository.findAll();
            List<TransactionHistoryDTO> transactionHistoryDTO = new ArrayList<>();
            for (TransactionHistory object: transactionHistory  ){
                transactionHistoryDTO.add(new TransactionHistoryDTO(object));
            }
            return transactionHistoryDTO;
        }catch (Exception e){
            logger.error("Failed to fetch all transactions", e);
            throw new RuntimeException("Failed to fetch all transactions", e);
        }
    }
    public List<TransactionHistoryDTO> getAllTransactionHistoryByAmount(int amount){
        try{
            List<TransactionHistory> transactionHistory = transactionHistoryRepository.findTransactionHistoryByAmount(amount);
            List<TransactionHistoryDTO> transactionHistoryDTO = new ArrayList<>();
            for (TransactionHistory object: transactionHistory  ){
                transactionHistoryDTO.add(new TransactionHistoryDTO(object));
            }
            return transactionHistoryDTO;
        }catch (Exception e){
            logger.error("Failed to fetch all transactions by amount {}",amount);
            throw new RuntimeException("Failed to fetch all transactions", e);
        }
    }
    public List<TransactionHistoryDTO> getAllTransactionHistoryBySender(String senderid){
        try{
            List<TransactionHistory> transactionHistory = transactionHistoryRepository.findTransactionHistoryBySender(senderid);
            List<TransactionHistoryDTO> transactionHistoryDTO = new ArrayList<>();
            for (TransactionHistory object: transactionHistory  ){
                transactionHistoryDTO.add(new TransactionHistoryDTO(object));
            }
            return transactionHistoryDTO;
        }catch (Exception e){
            logger.error("Failed to fetch all transactions by sender with id: {}",senderid);
            throw new RuntimeException("Failed to fetch all transactions", e);
        }
    }
    public List<TransactionHistoryDTO> getAllTransactionHistoryByRecipient(String recipientid){
        try{
            List<TransactionHistory> transactionHistory = transactionHistoryRepository.findTransactionHistoryBySender(recipientid);
            List<TransactionHistoryDTO> transactionHistoryDTO = new ArrayList<>();
            for (TransactionHistory object: transactionHistory  ){
                transactionHistoryDTO.add(new TransactionHistoryDTO(object));
            }
            return transactionHistoryDTO;
        }catch (Exception e){
            logger.error("Failed to fetch all transactions by recipient with id: {}",recipientid);
            throw new RuntimeException("Failed to fetch all transactions", e);
        }
    }


}
