package com.project.service;

import com.project.exceptions.custom_exceptions.BusinessLogic.BalanceNotFoundException;
import com.project.exceptions.custom_exceptions.BusinessLogic.InsufficientBalanceException;
import com.project.exceptions.custom_exceptions.BusinessLogic.TransferFailedException;
import com.project.exceptions.custom_exceptions.BusinessLogic.UserNotFoundException;
import com.project.exceptions.custom_exceptions.Database.DatabaseException;
import com.project.model.BalanceInformation;
import com.project.model.User;
import com.project.payload.dto.BalanceInformationDTO;
import com.project.repository.BalanceInformationRepository;
import com.project.repository.UserRepository;
import jakarta.persistence.PessimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BalanceInformationService {
    private final Logger logger = LoggerFactory.getLogger(BalanceInformationService.class);
    @Autowired
    BalanceInformationRepository balanaceInformationRepository;
    @Autowired
    UserRepository userRepository;

    public BalanceInformationDTO getUserBalanceInformationByUsername(String username) {
//        if(username == null){
//            throw new MissingRequiredFieldException("username");
//        }
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username:" + username + " not found"));
            System.out.println(user);
            BalanceInformation balanceInformation = balanaceInformationRepository.findBalanceInformationByUser(user).orElseThrow(() -> new BalanceNotFoundException("No balance information for username:" + username));
            System.out.println(balanceInformation);

            BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
            logger.info("Fetched balance of user {} from database", balanceInformationDTO.getUser());

            return balanceInformationDTO;
        } catch (Exception e) {
            logger.error("Failed to fetch balance of user  from database.", e);
            throw new DatabaseException("Error while retrieving user", e);
        }


    }

    public BalanceInformationDTO getUserBalanceInformationByPhone(String phone) {
        BalanceInformation balanceInformation = balanaceInformationRepository.findBalanceInformationsByPhonenumber(phone).orElseThrow(() -> new BalanceNotFoundException("balance information with phone " + phone + " not found"));
        BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
        logger.info("Fetched balance of user {} from database", balanceInformationDTO.getUser());
        return balanceInformationDTO;

    }

    public void saveBalanceInformation(BalanceInformationDTO balanceInformationDTO) {
        try {
            User user = userRepository.getReferenceById(balanceInformationDTO.getUser());
            BalanceInformation balanceInformation = new BalanceInformation(balanceInformationDTO);
            balanceInformation.setUser(user);
            balanaceInformationRepository.save(balanceInformation);

            logger.info("Saved balance of user {} to database", balanceInformationDTO.getUser());
        } catch (Exception e) {
            logger.error("Failed to Save balance of user to database.", e);
            throw new RuntimeException("Failed to Save balance of user to database", e);
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {RuntimeException.class, PessimisticLockException.class})
    public Optional<BalanceInformationDTO> IncreaseBalance(String username, Integer amount) {


        BalanceInformation balanceInformation;
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username:" + username + " not found"));
        balanceInformation = balanaceInformationRepository.findBalanceInformationByUser(user).orElseThrow(() -> new BalanceNotFoundException("No balance information for username:" + username));

        int current_amount = balanceInformation.getBalance_amount();
        int after_amount = current_amount - amount;
        balanceInformation.setBalance_amount(after_amount);
        BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
        try {
            balanaceInformationRepository.updateBalanceWithID(balanceInformation.getId(), after_amount);
            return Optional.of(balanceInformationDTO);
        } catch (RuntimeException e) {
            logger.error("can't increase balance information with phone {}", username);
            throw new TransferFailedException("can't increase balance information");
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {RuntimeException.class, PessimisticLockException.class})
    public Optional<BalanceInformationDTO> DecreaseBalance(String username, int amount) {
        BalanceInformation balanceInformation;
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username:" + username + " not found"));
        balanceInformation = balanaceInformationRepository.findBalanceInformationByUser(user).orElseThrow(() -> new BalanceNotFoundException("No balance information for username:" + username));


        int current_amount = balanceInformation.getBalance_amount();
        if (current_amount == 0) {
            logger.error("Insufficient balance for user: {}", username);
            throw new InsufficientBalanceException("Insufficient balance");
        }
        int after_amount = current_amount - amount;

        balanceInformation.setBalance_amount(after_amount);
        BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
        try {
            balanaceInformationRepository.updateBalanceWithID(balanceInformation.getId(), after_amount);
            return Optional.of(balanceInformationDTO);
        } catch (RuntimeException e) {
            logger.error("can't decrease balance information with username {}", username);
            throw new TransferFailedException("can't decrease balance information");
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {RuntimeException.class, PessimisticLockException.class})
    public List<Optional<BalanceInformationDTO>> TransferBalance(String username, String phone, int amount) {
        BalanceInformation senderBalanceInformation;
        BalanceInformation recipientBalanceInformation;

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username:" + username + " not found"));

        senderBalanceInformation = balanaceInformationRepository.findBalanceInformationByUser(user).orElseThrow(() -> new BalanceNotFoundException("No balance information for username:" + username));
        recipientBalanceInformation = balanaceInformationRepository.findBalanceInformationsByPhonenumber(phone).orElseThrow(() -> new BalanceNotFoundException("balance information with phone " + phone + " not found"));


        int current_sender_amount = senderBalanceInformation.getBalance_amount();
        if (current_sender_amount == 0) {
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
            balanaceInformationRepository.updateBalanceWithID(senderBalanceInformation.getId(), after_sender_amount);
            balanaceInformationRepository.updateBalanceWithPhone(recipientBalanceInformation.getPhone_number(), after_recipient_amount);
            return List.of(Optional.of(senderBalanceInformationDTO), Optional.of(recipientBalanceInformationDTO));
        } catch (RuntimeException e) {
            logger.error("can't transfer balance");
            throw new TransferFailedException("failed to transfer balance");
        }
    }
}
