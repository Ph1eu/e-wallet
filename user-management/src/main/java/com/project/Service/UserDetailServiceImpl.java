package com.project.Service;

import com.project.Model.BalanceInformation;
import com.project.Model.User;
import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Payload.DTO.UserDTO;
import com.project.Repository.AddressRepository;
import com.project.Repository.BalanceInformationRepository;
import com.project.Repository.PaymentcardRepository;
import com.project.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentcardRepository paymentcardRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    BalanceInformationRepository balanceInformationRepository;
    private final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return CustomUserDetail.build(user);
    }

    public UserDetails loadUserByIdEmail(String idemail)throws UsernameNotFoundException{
        User user = userRepository.findById(idemail).
                orElseThrow(() -> new UsernameNotFoundException("User Not Found with Id_email: " + idemail));
        return CustomUserDetail.build(user);

    }
    public UserDTO getUserWithBalanceInformation(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = new UserDTO(user);

            Optional<BalanceInformation> balanceInformationOptional = balanceInformationRepository.findBalanceInformationsByUserId(user.getId_email());
            if (balanceInformationOptional.isPresent()) {
                BalanceInformation balanceInformation = balanceInformationOptional.get();
                BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
                userDTO.setBalanceInformation(balanceInformationDTO);
            }

            return userDTO;
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }


    private List<UserDTO> wrapUserDTOS(List<Object[]> result) {
        List<UserDTO> userDTOs = new ArrayList<>();

        for (Object[] row : result) {
            User user = (User) row[0];
            BalanceInformation balanceInformation = (BalanceInformation) row[1];

            UserDTO userDTO = new UserDTO(user);
            if (balanceInformation != null) {
                BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
                userDTO.setBalanceInformation(balanceInformationDTO);
            }

            userDTOs.add(userDTO);
        }
        return userDTOs;
    }
    public List<UserDTO> getAllUsersWithEmailandBalance(String email,Integer balance){
        List<Object[]> result = userRepository.findUserWithEmailandBalance(email,balance);
        return wrapUserDTOS(result);
    }
    public List<UserDTO> getAllUsersWithEmail(String email){
        List<Object[]> result = userRepository.findUserWithEmail(email);
        return wrapUserDTOS(result);
    }
    public List<UserDTO> getAllUsersWithBalance(Integer balance){
        List<Object[]> result = userRepository.findUserWithBalance(balance);
        return wrapUserDTOS(result);
    }
    public List<UserDTO> getAllUsersWithBalanceInformation() {
        List<Object[]> result = userRepository.findAllUsersWithBalanceInformation();
        return wrapUserDTOS(result);
    }
        public void saveUser(UserDTO userDTO){
        try{
            userRepository.save(new User(userDTO));
            logger.info("Save user successfully for user {}",userDTO.getUsername());
        }
        catch (Exception e){
            logger.error("Failed to save user {}",userDTO.getUsername());
            throw new RuntimeException("Failed to save user.", e);
        }
    }
    public boolean existByUsername(String username){
        try {
            if (userRepository.existsByUsername(username)) {
                logger.info(" username exist {}",username);
                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            logger.error("username doesn't exist {}",username);
            throw new RuntimeException("username doesn't exist.", e);
        }
    }
    public boolean existByIdemail(String email){
        try {
            if (userRepository.existsByIdemail(email)) {
                logger.info(" username exist {}",email);
                return true;
            }
            else{
                return false;
            }
        }catch (Exception e){
            logger.error("username doesn't exist {}",email);
            throw new RuntimeException("username doesn't exist.", e);
        }
    }
    public List<UserDTO> findAll(){
        try{
            List<User> users = userRepository.findAll();
            List<UserDTO> userDTOS = new ArrayList<>();
            for(User user:users){
                userDTOS.add(new UserDTO(user));
            }
            logger.info("Fetched all users {}");
            return userDTOS;
        }
        catch (Exception e){
            logger.error("Failed to fetch all users");
            throw new RuntimeException("Failed to fetch all users.", e);
        }

    }

}
