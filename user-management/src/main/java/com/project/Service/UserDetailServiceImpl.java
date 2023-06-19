package com.project.Service;

import com.project.Model.User;
import com.project.Payload.DTO.UserDTO;
import com.project.Repository.AddressRepository;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentcardRepository paymentcardRepository;
    @Autowired
    AddressRepository addressRepository;
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
