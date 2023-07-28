package com.project.Service;

import com.project.Exceptions.CustomExceptions.BusinessLogic.InsufficientBalanceException;
import com.project.Exceptions.CustomExceptions.BusinessLogic.TransferFailedException;
import com.project.Exceptions.CustomExceptions.BusinessLogic.UserNotFoundException;
import com.project.Exceptions.CustomExceptions.Database.DatabaseException;
import com.project.Exceptions.CustomExceptions.Database.EmptyResultDataAccessException;
import com.project.Model.BalanceInformation;
import com.project.Model.User;
import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Repository.BalanceInformationRepository;
import com.project.Repository.UserRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PessimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BalanceInformationService {
    @Autowired
    BalanceInformationRepository balanaceInformationRepository;
    @Autowired
    UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(BalanceInformationService.class);

    public BalanceInformationDTO getUserBalanceInformationByUsername(String  username)
    {
//        if(username == null){
//            throw new MissingRequiredFieldException("username");
//        }
        try
            {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username:"+username+" not found"));
            System.out.println(user);
            BalanceInformation balanceInformation = balanaceInformationRepository.findBalanceInformationByUser(user);
            System.out.println(balanceInformation);

            BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
            logger.info("Fetched balance of user {} from database", balanceInformationDTO.getUser());

            return balanceInformationDTO;
    }
    catch(Exception e)
    {
        logger.error("Failed to fetch balance of user  from database.", e);
        throw new DatabaseException("Error while retrieving user", e);
    }


    }
    public BalanceInformationDTO getUserBalanceInformationByPhone(String  phone)
    {

        try
        {

            BalanceInformation balanceInformation = balanaceInformationRepository.findBalanceInformationsByPhonenumber(phone).orElseThrow(() -> new UserNotFoundException("balance information with phone "+phone+" not found"));
            System.out.println(balanceInformation);

            BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
            logger.info("Fetched balance of user {} from database", balanceInformationDTO.getUser());

            return balanceInformationDTO;
        }
        catch(Exception e)
        {
            logger.error("Failed to fetch balance of user  from database.", e);
            throw new RuntimeException("Failed to fetch balance of user  from database.", e);
        }


    }
    public  void saveBalanceInformation(BalanceInformationDTO balanceInformationDTO){
        try{
            User user = userRepository.getReferenceById(balanceInformationDTO.getUser());
            BalanceInformation balanceInformation = new BalanceInformation(balanceInformationDTO);
            balanceInformation.setUser(user);
            balanaceInformationRepository.save(balanceInformation);

            logger.info("Saved balance of user {} to database", balanceInformationDTO.getUser());
        }
        catch(Exception e)
        {
            logger.error("Failed to Save balance of user to database.", e);
            throw new RuntimeException("Failed to Save balance of user to database", e);
        }
    }
    @Transactional(isolation = Isolation.SERIALIZABLE,rollbackFor = {RuntimeException.class,PessimisticLockException.class})
    public Optional<BalanceInformationDTO> IncreaseBalance( String username,  Integer amount){


        BalanceInformation balanceInformation;
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            balanceInformation = balanaceInformationRepository.findBalanceInformationByUser(user);
        } else {
            logger.error("User not found with username: {}", username);
            throw new UserNotFoundException("User not found");
        }

        int current_amount = balanceInformation.getBalance_amount();
        int after_amount = current_amount - amount;
        balanceInformation.setBalance_amount(after_amount);
        BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
        try {
            balanaceInformationRepository.updateBalanceWithID(balanceInformation.getId(),after_amount);
            return Optional.of(balanceInformationDTO);
        }catch (RuntimeException e){
            logger.error("can't increase balance information with phone {}",username);
            throw  new TransferFailedException("can't increase balance information");
        }
    }
    @Transactional(isolation = Isolation.SERIALIZABLE,rollbackFor = {RuntimeException.class, PessimisticLockException.class})
    public Optional<BalanceInformationDTO> DecreaseBalance( String username,  int amount){
        BalanceInformation balanceInformation;
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            balanceInformation = balanaceInformationRepository.findBalanceInformationByUser(user);
        } else {
            logger.error("User not found with username: {}", username);
            throw new UserNotFoundException("User not found");
        }

        int current_amount = balanceInformation.getBalance_amount();
        if (current_amount == 0) {
            logger.error("Insufficient balance for user: {}", username);
            throw new InsufficientBalanceException("Insufficient balance");
        }
        int after_amount = current_amount - amount;

        balanceInformation.setBalance_amount(after_amount);
        BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
        try {
            balanaceInformationRepository.updateBalanceWithID(balanceInformation.getId(),after_amount);
            return Optional.of(balanceInformationDTO);
        }catch (RuntimeException e){
            logger.error("can't decrease balance information with username {}",username);
            throw new TransferFailedException("can't decrease balance information");
        }
    }
    @Transactional(isolation = Isolation.SERIALIZABLE,rollbackFor = {RuntimeException.class,PessimisticLockException.class})
    public List<Optional<BalanceInformationDTO>> TransferBalance(String username, String phone , int amount){
        BalanceInformation senderBalanceInformation;
        BalanceInformation recipientBalanceInformation;

        Optional<User> optionalSender = userRepository.findByUsername(username);
        if (optionalSender.isPresent()) {
            User user = optionalSender.get();
            senderBalanceInformation = balanaceInformationRepository.findBalanceInformationByUser(user);
            recipientBalanceInformation = balanaceInformationRepository.findBalanceInformationsByPhonenumber(phone).orElseThrow(() -> new EmptyResultDataAccessException("balance information with phone "+phone+" not found"));
        } else {
            logger.error("User not found with username: {}", username);
            throw new UserNotFoundException("User not found");
        }

        int current_sender_amount = senderBalanceInformation.getBalance_amount();
        if (current_sender_amount == 0){
            throw new InsufficientBalanceException("Sender doesn't have sufficient balance");
        }
        int current_recipient_amount = recipientBalanceInformation.getBalance_amount();

        int after_sender_amount = current_sender_amount - amount;
        int after_recipient_amount = current_recipient_amount + amount;

        senderBalanceInformation.setBalance_amount(after_sender_amount);
        recipientBalanceInformation.setBalance_amount(after_recipient_amount);
        BalanceInformationDTO senderBalanceInformationDTO = new BalanceInformationDTO(senderBalanceInformation);
        BalanceInformationDTO recipientBalanceInformationDTO = new BalanceInformationDTO(recipientBalanceInformation);

        try {
            balanaceInformationRepository.updateBalanceWithID(senderBalanceInformation.getId(),after_sender_amount);
            balanaceInformationRepository.updateBalanceWithPhone(recipientBalanceInformation.getPhone_number(),after_recipient_amount);
            return List.of(Optional.of(senderBalanceInformationDTO),Optional.of(recipientBalanceInformationDTO));
        }catch (RuntimeException e){
            logger.error("can't transfer balance");
            throw  new TransferFailedException("failed to transfer balance");
        }
    }
}
