package com.project.Service;

import com.project.Exceptions.CustomExceptions.BusinessLogic.UserNotFoundException;
import com.project.Model.User;
import com.project.Payload.DTO.UserDTO;
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

    private final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return CustomUserDetail.build(new UserDTO(user));
    }



    public UserDTO findByUsername(String username){
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException("User Not Found with username: " + username));
        return new UserDTO(user);
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

}
