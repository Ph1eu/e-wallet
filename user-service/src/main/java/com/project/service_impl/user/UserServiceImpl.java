package com.project.service_impl.user;

import com.project.service.balanceinformation.entity.BalanceInformation;
import com.project.service.paymentcard.entity.Paymentcard;
import com.project.service.user.UserService;
import com.project.service.user.dto.UserFilterDto;
import com.project.service.user.dto.UserPageDto;
import com.project.service.user.entity.User;
import com.project.service.balanceinformation.dto.BalanceInformationDto;
import com.project.service.paymentcard.dto.PaymentcardDTO;
import com.project.service.user.dto.UserDto;
import com.project.service_impl.address.AddressRepository;
import com.project.service_impl.balanceinformation.BalanceInformationRepository;
import com.project.service_impl.paymentcard.PaymentcardRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentcardRepository paymentcardRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private BalanceInformationRepository balanceInformationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return CustomUserDetail.build(user);
    }

    public UserDto getUserWithBalanceInformation(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
        UserDto userDTO = new UserDto(user);
        List<PaymentcardDTO> paymentcardDTOS = new ArrayList<>();
        for (Paymentcard paymentcard : user.getPaymentcards()) {
            paymentcardDTOS.add(new PaymentcardDTO(paymentcard));
        }
        userDTO.setPaymentcardsDTO(paymentcardDTOS);
        for (PaymentcardDTO paymentcardDTO : userDTO.getPaymentcardsDTO()) {
            paymentcardDTO.setUser(userDTO);
        }
        Optional<BalanceInformation> balanceInformationOptional = balanceInformationRepository.findBalanceInformationsByUserId(user.getEmail());
        if (balanceInformationOptional.isPresent()) {
            BalanceInformation balanceInformation = balanceInformationOptional.get();
            BalanceInformationDto balanceInformationDTO = new BalanceInformationDto(balanceInformation);
            userDTO.setBalanceInformation(balanceInformationDTO);
        }

        return userDTO;

    }


    private Page<UserDto> wrapUserDTOS(Page<Object[]> result) {
        List<UserDto> userDtos = new ArrayList<>();

        for (Object[] row : result.getContent()) {
            User user = (User) row[0];
            BalanceInformation balanceInformation = (BalanceInformation) row[1];

            UserDto userDTO = new UserDto(user);
            List<PaymentcardDTO> paymentcardDTOS = new ArrayList<>();
            for (Paymentcard paymentcard : user.getPaymentcards()) {
                paymentcardDTOS.add(new PaymentcardDTO(paymentcard));
            }
            userDTO.setPaymentcardsDTO(paymentcardDTOS);
            for (PaymentcardDTO paymentcard : userDTO.getPaymentcardsDTO()) {
                paymentcard.setUser(userDTO);
            }
            if (balanceInformation != null) {
                BalanceInformationDto balanceInformationDTO = new BalanceInformationDto(balanceInformation);
                userDTO.setBalanceInformation(balanceInformationDTO);
            }

            userDtos.add(userDTO);
        }

        return new PageImpl<>(userDtos, result.getPageable(), result.getTotalElements());
    }

    public Page<UserDto> getAllUsersWithEmailandBalance(String email, Integer balance, Pageable pageable) {
        Page<Object[]> result = userRepository.findUserWithEmailandBalance(email, balance, pageable);
        return wrapUserDTOS(result);
    }

    public Page<UserDto> getAllUsersWithEmail(String email, Pageable pageable) {
        Page<Object[]> result = userRepository.findUserWithEmail(email, pageable);
        return wrapUserDTOS(result);
    }

    public Page<UserDto> getAllUsersWithBalance(Integer balance, Pageable pageable) {
        Page<Object[]> result = userRepository.findUserWithBalance(balance, pageable);
        return wrapUserDTOS(result);
    }

    public Page<UserDto> getAllUsersWithBalanceInformation(Pageable pageable) {
        Page<Object[]> result = userRepository.findAllUsersWithBalanceInformation(pageable);
        return wrapUserDTOS(result);
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(String id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            logger.error("Failed to find user by ID {}", id);
            throw new RuntimeException("Failed to find user by ID {}");
        }
    }
    @Transactional(readOnly = true)
    @Override
    public User getById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }
    @Transactional(readOnly = true)
    @Override
    public UserPageDto list(UserFilterDto filter) {
        return null;
    }
    @Override
    public void deleteById(String id) {

    }

    @Override
    public void create(UserDto user) {
        try {
            userRepository.save(new User(user));
            logger.info("Create user successfully for user {}", user.getUsername());
        } catch (Exception e) {
            logger.error("Failed to create user {}", user.getUsername());
            throw new RuntimeException("Failed to create user.", e);
        }
    }


    @Override
    public void update(UserDto user) {

    }

    @Override
    public boolean existsById(String id) {
        try {
            if (userRepository.existsById(id)) {
                logger.info(" username exist with id:{}", id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("username doesn't exist with id:{}", id);
            throw new RuntimeException("username doesn't exist.", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
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

    @Override
    public boolean existsByUsername(String username) {
        try {
            if (userRepository.existsByUsername(username)) {
                logger.info(" username exist {}", username);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("username doesn't exist {}", username);
            throw new RuntimeException("username doesn't exist.", e);
        }
    }
}
