package com.project.service_impl.user;

import com.project.service.role.dto.ERole;
import com.project.service.user.UserService;
import com.project.service.user.dto.UserFilterDto;
import com.project.service.user.dto.UserPageDto;
import com.project.service.user.dto.UserUpdateDto;
import com.project.service.user.entity.User;
import com.project.service.user.dto.UserDto;
import com.project.service.user.mapper.UserMapper;
import com.project.service_impl.address.AddressRepository;
import com.project.service_impl.balanceinformation.BalanceInformationRepository;
import com.project.service_impl.paymentcard.PaymentcardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private UserMapper userMapper;
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
        logger.info("Requested to find user by ID {}", id);
        Optional<User> user = userRepository.findById(id);
        logger.debug("Successfully found user by ID {}", id);
        return user;

    }
    @Transactional(readOnly = true)
    @Override
    public User getById(String id) {
        logger.info("Requested to get user by ID {}", id);
        User user = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User not found with ID: " + id));
        logger.debug("Successfully got user by ID {}", id);
        return user;
    }
    @Transactional(readOnly = true)
    @Override
    public UserPageDto list(UserFilterDto filter) {
        logger.info("Requested to list users by filter {}", filter);
        Specification<User> spec = Specification.where(UserSpecifications.hasUsername(filter.username()))
                .and(UserSpecifications.hasEmail(filter.email()));
        Pageable pageable = PageRequest.of(filter.page(), filter.size());
        Page<User> users = userRepository.findAll(spec, pageable);
        List<UserDto> userDtos = users.stream().map(userMapper::userToUserDto).collect(Collectors.toList());
        logger.debug("Successfully Listed users by filter {}", filter);
        return new UserPageDto( pageable.getPageNumber(), pageable.getPageSize(),userDtos);
    }
    @Override
    @Transactional
    public void deleteById(String id) {
        logger.info("Requested to delete user by ID {}", id);
        userRepository.deleteById(id);
        logger.debug("Successfully deleted user by ID {}", id);
    }
    @Transactional
    @Override
    public User create(UserDto userDto) {
        logger.info("Requested to create user for user {}", userDto.toString());
        if(existsByUsername(userDto.username())){
            logger.error("Username already exists");
            throw new RuntimeException("Username already exists");
        }
        if(existsByEmail(userDto.email())){
            logger.error("Email already exists");
            throw new RuntimeException("Email already exists");
        }
        if(existsById(userDto.id())){
            logger.error("User already exists");
            throw new RuntimeException("User already exists");
        }
        User user = userMapper.userDtoToUser(userDto);
        userRepository.save(user);
        logger.debug("Successfully Create user {}", user.getUsername());
        return user;

    }
    @Override
    @Transactional
    public void update(String userId,UserUpdateDto userUpdateDto) {
            logger.info("Requested to update user for user {}", userUpdateDto.toString());
            if (!existsById(userId)) {
                logger.error("User doesn't exist with id:{}", userId);
                throw new RuntimeException("User doesn't exist.");
            }
            User user = getById(userId);
            if(userUpdateDto.username()!=null){
                if(existsByUsername(userUpdateDto.username())){
                    logger.error("Username already exists");
                    throw new RuntimeException("Username already exists");
                }
                user.setUsername(userUpdateDto.username());
            }
            if(userUpdateDto.email()!=null){
                if(existsByEmail(userUpdateDto.email())){
                    logger.error("Email already exists");
                    throw new RuntimeException("Email already exists");
                }
                user.setEmail(userUpdateDto.email());
            }
            if (userUpdateDto.first_name() != null) {
                user.setFirst_name(userUpdateDto.first_name());
            }
            if (userUpdateDto.last_name() != null) {
                user.setLast_name(userUpdateDto.last_name());
            }
            if (userUpdateDto.password() != null) {
                String password =passwordEncoder.encode(userUpdateDto.password());
                user.setPassword(password);
            }
            if(userUpdateDto.role()!=null){
                if(userUpdateDto.role().equals("admin")){
                    user.setRole(ERole.ROLE_ADMIN);
                }
                else if(userUpdateDto.role().equals("user")){
                    user.setRole(ERole.ROLE_USER);
                }
            }
            userRepository.save(user);
            logger.debug("Successfully updated user for user {}", userUpdateDto.toString());
    }
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String id) {
        logger.info("Requested to check user exist with id:{}", id);
        if (userRepository.existsById(id)) {
            logger.debug("User exist with id:{}", id);
            return true;
        } else {
            logger.debug("User doesn't exist with id:{}", id);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        logger.info("Checking email exist {}", email);
        if (userRepository.existsByEmail(email)) {
            logger.debug(" username exist {}", email);
            return true;
        } else {
            logger.debug(" username doesn't exist {}", email);
            return false;
        }

    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        logger.info("Checking username exist {}", username);
        if (userRepository.existsByUsername(username)) {
            logger.debug(" username exist {}", username);
            return true;
        } else {
            logger.debug(" username doesn't exist {}", username);
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        logger.info("Requested to get user by username {}", username);
        Optional<User> user = userRepository.findByUsername(username);
        logger.debug("Successfully found user by username {}", username);
        return user.orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}
