package com.project.service;

import com.project.exceptions.custom_exceptions.BusinessLogic.UserNotFoundException;
import com.project.model.*;
import com.project.payload.dto.TransactionHistoryDTO;
import com.project.repository.BalanceInformationRepository;
import com.project.repository.TransactionHistoryRepository;
import com.project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
        User sender = userRepository.findById(transactionHistoryDTO.getSenderid()).orElseThrow(()-> new UserNotFoundException("Sender for transaction not found"));
        User recipient = userRepository.findById(transactionHistoryDTO.getRecipientid()).orElseThrow(()-> new UserNotFoundException("Recipient for transaction not found"));
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
    public Page<TransactionHistoryDTO> getAllTransactionHistory(Pageable pageable){
        try{
            Page<TransactionHistory> result = transactionHistoryRepository.findAll(pageable);
//            int pageSize = pageable.getPageSize();
//            int offset = pageable.getPageNumber() * pageSize;
//            Page<TransactionHistory> result= new PageImpl<>(transactionHistories.subList(start, end), pageable, transactionHistories.size());
//            System.out.println(result);
            List<TransactionHistoryDTO> transactionHistoryDTO = new ArrayList<>();
            for (TransactionHistory object: result.getContent()  ){
                transactionHistoryDTO.add(new TransactionHistoryDTO(object));
            }
            return new PageImpl<>(transactionHistoryDTO, result.getPageable(), result.getTotalElements());

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
    public Page<TransactionHistoryDTO> getAllTransactionHistoryByFilter(Date startDate,Date endDate, String type,Pageable pageable){
        try{
            Page<TransactionHistory> result = transactionHistoryRepository.findTransactionHistoryByFilters(startDate,endDate,type,pageable);
            List<TransactionHistoryDTO> transactionHistoryDTO = new ArrayList<>();
            for (TransactionHistory object: result.getContent()  ){
                transactionHistoryDTO.add(new TransactionHistoryDTO(object));
            }
            return new PageImpl<>(transactionHistoryDTO, result.getPageable(), result.getTotalElements());
        }catch (Exception e){
            logger.error("Failed to fetch all transactions by recipient with filters");
            throw new RuntimeException("Failed to fetch all transactions", e);
        }
    }
    public Page<TransactionHistoryDTO> getAllTransactionHistoryByRange(Date startDate, Date endDate, Pageable pageable){
        try{
            Page<TransactionHistory> result = transactionHistoryRepository.findTransactionHistoryByRange(startDate,endDate,pageable);
            List<TransactionHistoryDTO> transactionHistoryDTO = new ArrayList<>();
            for (TransactionHistory object: result.getContent()  ){
                transactionHistoryDTO.add(new TransactionHistoryDTO(object));
            }
            return new PageImpl<>(transactionHistoryDTO, result.getPageable(), result.getTotalElements());

        }catch (Exception e){
            logger.error("Failed to fetch all transactions by recipient with given range");
            throw new RuntimeException("Failed to fetch all transactions", e);
        }
    }
    public Page<TransactionHistoryDTO> getAllTransactionHistoryByStartDate(Date startDate,Pageable pageable){
        try{
            Page<TransactionHistory> result = transactionHistoryRepository.findTransactionHistoryByStartDate(startDate,pageable);
            List<TransactionHistoryDTO> transactionHistoryDTO = new ArrayList<>();
            for (TransactionHistory object: result.getContent()  ){
                transactionHistoryDTO.add(new TransactionHistoryDTO(object));
            }
            return new PageImpl<>(transactionHistoryDTO, result.getPageable(), result.getTotalElements());
        }catch (Exception e){
            logger.error("Failed to fetch all transactions with given start date");
            throw new RuntimeException("Failed to fetch all transactions", e);
        }
    }
    public Page<TransactionHistoryDTO> getAllTransactionHistoryByEndDate(Date endDate,Pageable pageable){
        try{
            Page<TransactionHistory> result = transactionHistoryRepository.findTransactionHistoryByEndDate(endDate,pageable);
            List<TransactionHistoryDTO> transactionHistoryDTO = new ArrayList<>();
            for (TransactionHistory object: result.getContent()  ){
                transactionHistoryDTO.add(new TransactionHistoryDTO(object));
            }
            return new PageImpl<>(transactionHistoryDTO, result.getPageable(), result.getTotalElements());
        }catch (Exception e){
            logger.error("Failed to fetch all transactions with given end date");
            throw new RuntimeException("Failed to fetch all transactions", e);
        }
    }
    public Page<TransactionHistoryDTO> getAllTransactionHistoryByType(String type,Pageable page){
        try{
            Page<TransactionHistory> result = transactionHistoryRepository.findTransactionHistoryByTransactionType(type,page);
            List<TransactionHistoryDTO> transactionHistoryDTO = new ArrayList<>();
            for (TransactionHistory object: result.getContent()  ){
                transactionHistoryDTO.add(new TransactionHistoryDTO(object));
            }
            return new PageImpl<>(transactionHistoryDTO, result.getPageable(), result.getTotalElements());
        }catch (Exception e){
            logger.error("Failed to fetch all transactions by recipient with type ");
            throw new RuntimeException("Failed to fetch all transactions", e);
        }
    }

}
