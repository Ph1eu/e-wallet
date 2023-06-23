package com.project.Service;

import com.project.Model.BalanceInformation;
import com.project.Model.User;
import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Repository.BalanceInformationRepository;
import com.project.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        try
            {
            User user = userRepository.findByUsername(username).orElseThrow();
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
        throw new RuntimeException("Failed to fetch balance of user  from database.", e);
    }


    }
    public BalanceInformationDTO getUserBalanceInformationByPhone(String  phone)
    {
        try
        {

            BalanceInformation balanceInformation = balanaceInformationRepository.findBalanceInformationsByPhonenumber(phone);
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
}
