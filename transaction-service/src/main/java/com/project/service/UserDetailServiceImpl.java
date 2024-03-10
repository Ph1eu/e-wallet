package com.project.service;

import com.project.exceptions.custom_exceptions.BusinessLogic.UserNotFoundException;
import com.project.model.User;
import com.project.payload.dto.UserDTO;
import com.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(UserDetailServiceImpl.class);
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return CustomUserDetail.build(new UserDTO(user));
    }


    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException("User Not Found with username: " + username));
        return new UserDTO(user);
    }

    public boolean existByIdemail(String email) {
        try {
            if (userRepository.existsByIdemail(email)) {
                logger.info(" username exist {}", email);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("username doesn't exist {}", email);
            throw new RuntimeException("username doesn't exist.", e);
        }
    }

}
