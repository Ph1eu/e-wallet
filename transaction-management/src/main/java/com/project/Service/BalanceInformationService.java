package com.project.Service;

import com.project.Model.BalanceInformation;
import com.project.Model.User;
import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Payload.DTO.UserDTO;
import com.project.Repository.BalanaceInformationRepository;
import com.project.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BalanceInformationService {
    @Autowired
    BalanaceInformationRepository balanaceInformationRepository;
    @Autowired
    UserRepository userRepository;
    public BalanceInformationDTO getUserBalanceInformation(String  username)
    {

        User user = userRepository.findByUsername(username).orElseThrow();
        System.out.println(user);
        BalanceInformation balanceInformation = balanaceInformationRepository.findBalanceInformationByUser(user);
        System.out.println(balanceInformation);

        BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
        balanceInformationDTO.setUser(new UserDTO(user));
        return balanceInformationDTO;
    }
}
