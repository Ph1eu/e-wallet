package com.project.service_impl.user;

import com.project.api.rest.security.CustomUserDetail;
import com.project.service.balanceinformation.entity.BalanceInformation;
import com.project.service.paymentcard.entity.Paymentcard;
import com.project.service.role.dto.ERole;
import com.project.service.role.entity.Role;
import com.project.service.user.UserService;
import com.project.service.user.dto.UserFilterDto;
import com.project.service.user.dto.UserPageDto;
import com.project.service.user.dto.UserUpdateDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements  UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentcardRepository paymentcardRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private BalanceInformationRepository balanceInformationRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

//
//
//    public UserDto getUserWithBalanceInformation(String userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
//        UserDto userDTO = new UserDto(user);
//        List<PaymentcardDTO> paymentcardDTOS = new ArrayList<>();
//        for (Paymentcard paymentcard : user.getPaymentcards()) {
//            paymentcardDTOS.add(new PaymentcardDTO(paymentcard));
//        }
//        userDTO.setPaymentcards(paymentcardDTOS);
//        for (PaymentcardDTO paymentcardDTO : userDTO.getPaymentcards()) {
//            paymentcardDTO.setUser(userDTO);
//        }
//        Optional<BalanceInformation> balanceInformationOptional = balanceInformationRepository.findBalanceInformationsByUserId(user.getEmail());
//        if (balanceInformationOptional.isPresent()) {
//            BalanceInformation balanceInformation = balanceInformationOptional.get();
//            BalanceInformationDto balanceInformationDTO = new BalanceInformationDto(balanceInformation);
//            userDTO.setBalanceInformation(balanceInformationDTO);
//        }
//
//        return userDTO;
//
//    }
//
//
//    private Page<UserDto> wrapUserDTOS(Page<Object[]> result) {
//        List<UserDto> userDtos = new ArrayList<>();
//
//        for (Object[] row : result.getContent()) {
//            User user = (User) row[0];
//            BalanceInformation balanceInformation = (BalanceInformation) row[1];
//
//            UserDto userDTO = new UserDto(user);
//            List<PaymentcardDTO> paymentcardDTOS = new ArrayList<>();
//            for (Paymentcard paymentcard : user.getPaymentcards()) {
//                paymentcardDTOS.add(new PaymentcardDTO(paymentcard));
//            }
//            userDTO.setPaymentcardsDTO(paymentcardDTOS);
//            for (PaymentcardDTO paymentcard : userDTO.getPaymentcardsDTO()) {
//                paymentcard.setUser(userDTO);
//            }
//            if (balanceInformation != null) {
//                BalanceInformationDto balanceInformationDTO = new BalanceInformationDto(balanceInformation);
//                userDTO.setBalanceInformation(balanceInformationDTO);
//            }
//
//            userDtos.add(userDTO);
//        }
//
//        return new PageImpl<>(userDtos, result.getPageable(), result.getTotalElements());
//    }
//
//    public Page<UserDto> getAllUsersWithEmailandBalance(String email, Integer balance, Pageable pageable) {
//        Page<Object[]> result = userRepository.findUserWithEmailandBalance(email, balance, pageable);
//        return wrapUserDTOS(result);
//    }
//
//    public Page<UserDto> getAllUsersWithEmail(String email, Pageable pageable) {
//        Page<Object[]> result = userRepository.findUserWithEmail(email, pageable);
//        return wrapUserDTOS(result);
//    }
//
//    public Page<UserDto> getAllUsersWithBalance(Integer balance, Pageable pageable) {
//        Page<Object[]> result = userRepository.findUserWithBalance(balance, pageable);
//        return wrapUserDTOS(result);
//    }
//
//    public Page<UserDto> getAllUsersWithBalanceInformation(Pageable pageable) {
//        Page<Object[]> result = userRepository.findAllUsersWithBalanceInformation(pageable);
//        return wrapUserDTOS(result);
//    }


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
        logger.info("Getting user by id {}", id);
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }
    @Transactional(readOnly = true)
    @Override
    public UserPageDto list(UserFilterDto filter) {

        Specification<User> spec = Specification.where(UserSpecifications.hasUsername(filter.getUsername()))
                .and(UserSpecifications.hasEmail(filter.getEmail()));
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        Page<User> users = userRepository.findAll(spec, pageable);
        List<UserDto> userDtos = users.stream().map(UserDto::new).collect(Collectors.toList());
        return new UserPageDto( pageable.getPageNumber(), pageable.getPageSize(),userDtos);
    }
    @Override
    @Transactional
    public void deleteById(String id) {
        logger.info("Deleting user by id {}", id);
        userRepository.deleteById(id);
        logger.debug("User deleted by id {}", id);
    }
    @Transactional
    @Override
    public void create(UserDto user) {
        logger.info("Creating user for user {}", user.toString());
        if(existsByUsername(user.getUsername())){
            logger.error("Username already exists");
            throw new RuntimeException("Username already exists");
        }
        if(existsByEmail(user.getEmail())){
            logger.error("Email already exists");
            throw new RuntimeException("Email already exists");
        }
        if(existsById(user.getId())){
            logger.error("User already exists");
            throw new RuntimeException("User already exists");
        }
        userRepository.save(new User(user));
        logger.debug("Create user successfully for user {}", user.getUsername());

    }
    @Override
    @Transactional
    public void update(String userId,UserUpdateDto userUpdateDto) {
            logger.info("Updating user for user {}", userUpdateDto.toString());
            if (!existsById(userId)) {
                logger.error("User doesn't exist with id:{}", userId);
                throw new RuntimeException("User doesn't exist.");
            }
            User user = getById(userId);
            if(userUpdateDto.getUsername()!=null){
                if(existsByUsername(userUpdateDto.getUsername())){
                    logger.error("Username already exists");
                    throw new RuntimeException("Username already exists");
                }
                user.setUsername(userUpdateDto.getUsername());
            }
            if(userUpdateDto.getEmail()!=null){
                if(existsByEmail(userUpdateDto.getEmail())){
                    logger.error("Email already exists");
                    throw new RuntimeException("Email already exists");
                }
                user.setEmail(userUpdateDto.getEmail());
            }
            if (userUpdateDto.getFirst_name() != null) {
                user.setFirst_name(userUpdateDto.getFirst_name());
            }
            if (userUpdateDto.getLast_name() != null) {
                user.setLast_name(userUpdateDto.getLast_name());
            }
            if (userUpdateDto.getPassword() != null) {
                String password =passwordEncoder.encode(userUpdateDto.getPassword());
                user.setPassword(password);
            }
            if(userUpdateDto.getRole()!=null){
                if(userUpdateDto.getRole().equals("admin")){
                    Role role = new Role(ERole.ROLE_ADMIN);
                    user.setRoles(role);
                }
                else if(userUpdateDto.getRole().equals("user")){
                    Role role = new Role(ERole.ROLE_USER);
                    user.setRoles(role);
                }
            }
            userRepository.save(user);
            logger.debug("Update user successfully for user {}", user.getUsername());
    }
    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        logger.info("Checking email exist {}", email);
        if (userRepository.existsByEmail(email)) {
            logger.info(" username exist {}", email);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        logger.info("Checking username exist {}", username);
        if (userRepository.existsByUsername(username)) {
            logger.info(" username exist {}", username);
            return true;
        } else {
            logger.info(" username doesn't exist {}", username);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        logger.info("Getting user by username {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}
