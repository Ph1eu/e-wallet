package com.project.service;

import com.project.model.BalanceInformation;
import com.project.model.Paymentcard;
import com.project.model.User;
import com.project.payload.dto.BalanceInformationDTO;
import com.project.payload.dto.PaymentcardDTO;
import com.project.payload.dto.UserDTO;
import com.project.repository.AddressRepository;
import com.project.repository.BalanceInformationRepository;
import com.project.repository.PaymentcardRepository;
import com.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
        UserDTO userDTO = new UserDTO(user);
        List<PaymentcardDTO> paymentcardDTOS = new ArrayList<>();
        for (Paymentcard paymentcard:user.getPaymentcards()){
            paymentcardDTOS.add(new PaymentcardDTO(paymentcard));
        }
        userDTO.setPaymentcardsDTO(paymentcardDTOS);
        for(PaymentcardDTO paymentcardDTO: userDTO.getPaymentcardsDTO()){
            paymentcardDTO.setUser(userDTO);
        }
        Optional<BalanceInformation> balanceInformationOptional = balanceInformationRepository.findBalanceInformationsByUserId(user.getId_email());
        if (balanceInformationOptional.isPresent()) {
            BalanceInformation balanceInformation = balanceInformationOptional.get();
            BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
            userDTO.setBalanceInformation(balanceInformationDTO);
        }

        return userDTO;

    }


    private Page<UserDTO> wrapUserDTOS(Page<Object[]> result) {
        List<UserDTO> userDTOs = new ArrayList<>();

        for (Object[] row : result.getContent()) {
            User user = (User) row[0];
            BalanceInformation balanceInformation = (BalanceInformation) row[1];

            UserDTO userDTO = new UserDTO(user);
            List<PaymentcardDTO> paymentcardDTOS = new ArrayList<>();
            for (Paymentcard paymentcard:user.getPaymentcards()){
                paymentcardDTOS.add(new PaymentcardDTO(paymentcard));
            }
            userDTO.setPaymentcardsDTO(paymentcardDTOS);
            for(PaymentcardDTO paymentcard:userDTO.getPaymentcards()){
                paymentcard.setUser(userDTO);
            }
            if (balanceInformation != null) {
                BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(balanceInformation);
                userDTO.setBalanceInformation(balanceInformationDTO);
            }

            userDTOs.add(userDTO);
        }

        return new PageImpl<>(userDTOs, result.getPageable(), result.getTotalElements());
    }

    public Page<UserDTO> getAllUsersWithEmailandBalance(String email, Integer balance, Pageable pageable){
        Page<Object[]> result = userRepository.findUserWithEmailandBalance(email,balance,pageable);
        return wrapUserDTOS(result);
    }
    public Page<UserDTO> getAllUsersWithEmail(String email,Pageable pageable){
        Page<Object[]> result = userRepository.findUserWithEmail(email,pageable);
        return wrapUserDTOS(result);
    }
    public Page<UserDTO> getAllUsersWithBalance(Integer balance,Pageable pageable){
        Page<Object[]> result = userRepository.findUserWithBalance(balance,pageable);
        return wrapUserDTOS(result);
    }
    public Page<UserDTO> getAllUsersWithBalanceInformation(Pageable pageable) {
        Page<Object[]> result = userRepository.findAllUsersWithBalanceInformation(pageable);
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
