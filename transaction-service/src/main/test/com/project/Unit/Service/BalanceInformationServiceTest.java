package com.project.Unit.Service;

import com.project.Exceptions.CustomExceptions.BusinessLogic.BalanceNotFoundException;
import com.project.Exceptions.CustomExceptions.BusinessLogic.InsufficientBalanceException;
import com.project.Model.BalanceInformation;
import com.project.Model.User;
import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Repository.BalanceInformationRepository;
import com.project.Repository.UserRepository;
import com.project.Service.BalanceInformationService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BalanceInformationServiceTest {
    @InjectMocks
    private BalanceInformationService balanceInformationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BalanceInformationRepository balanceInformationRepository;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);


    }

    @Test
    void getUserBalanceInformationByUsername() {
        User mockUser = Mockito.mock(User.class);
        mockUser = new User();
        mockUser.setIdemail("john.doe@example.com");
        mockUser.setUsername("johndoe");
        mockUser.setPassword("password123");
        mockUser.setFirst_name("John");
        mockUser.setLast_name("Doe");
        mockUser.setRegistration_date(new Date());
        mockUser.setAddress("123 Main St");
        mockUser.setRoles("ROLE_USER");
        mockUser.setPaymentcards(null);
        String username = "johndoe";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Mock the balanceInformationRepository.findBalanceInformationByUser method to return balance information
        BalanceInformation mockBalanceInformation = new BalanceInformation();
        mockBalanceInformation.setId("12345");
        mockBalanceInformation.setUser(mockUser);
        mockBalanceInformation.setBalance_amount(1000);
        mockBalanceInformation.setPhone_number("1234567890");

        Mockito.when(balanceInformationRepository.findBalanceInformationByUser(mockUser))
                .thenReturn(Optional.of(mockBalanceInformation));
        BalanceInformationDTO mockBalanceInformationDTO = Mockito.mock(BalanceInformationDTO.class);
        mockBalanceInformationDTO = new BalanceInformationDTO();
        mockBalanceInformationDTO.setId("12345");
       mockBalanceInformationDTO.setUser("john.doe@example.com");
      mockBalanceInformationDTO.setBalance_amount(1000);
       mockBalanceInformationDTO.setPhone_number("1234567890");

        // Call the method under test
        BalanceInformationDTO result = balanceInformationService.getUserBalanceInformationByUsername(username);


        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verify(balanceInformationRepository).findBalanceInformationByUser(mockUser);

        // Assert the result
        Assertions.assertNotNull(result);
       Assertions.assertEquals(mockBalanceInformationDTO, result);
    }

    @Test
    void getUserBalanceInformationByPhone() {
        String phone = "1234567890";

        User mockUser = new User();
        mockUser.setIdemail("john.doe@example.com");
        mockUser.setUsername("johndoe");
        mockUser.setPassword("password123");
        mockUser.setFirst_name("John");
        mockUser.setLast_name("Doe");
        mockUser.setRegistration_date(new Date());
        mockUser.setAddress("123 Main St");
        mockUser.setRoles("ROLE_USER");
        mockUser.setPaymentcards(null);

        // Mock the behavior of balanceInformationRepository.findBalanceInformationsByPhonenumber
        BalanceInformation mockBalanceInformation = new BalanceInformation();
        mockBalanceInformation.setId("12345");
        mockBalanceInformation.setUser(mockUser);
        mockBalanceInformation.setBalance_amount(1000);
        mockBalanceInformation.setPhone_number(phone);

        Mockito.when(balanceInformationRepository.findBalanceInformationsByPhonenumber(phone))
                .thenReturn(Optional.of(mockBalanceInformation));

        BalanceInformationDTO mockBalanceInformationDTO = Mockito.mock(BalanceInformationDTO.class);
        mockBalanceInformationDTO = new BalanceInformationDTO();
        mockBalanceInformationDTO.setId("12345");
        mockBalanceInformationDTO.setUser("john.doe@example.com");
        mockBalanceInformationDTO.setBalance_amount(1000);
        mockBalanceInformationDTO.setPhone_number("1234567890");

        // Call the method under test
        BalanceInformationDTO result = balanceInformationService.getUserBalanceInformationByPhone(phone);

        // Verify the expected interactions
        Mockito.verify(balanceInformationRepository).findBalanceInformationsByPhonenumber(phone);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockBalanceInformationDTO, result);
        Mockito.when(balanceInformationRepository.findBalanceInformationsByPhonenumber(phone))
                .thenReturn(Optional.empty());
        assertThrows(BalanceNotFoundException.class,() -> balanceInformationService.getUserBalanceInformationByPhone(phone));
    }

    @Test
    void saveBalanceInformation() {
        BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO();
        balanceInformationDTO.setId("12345");
        balanceInformationDTO.setUser("john.doe@example.com");
        balanceInformationDTO.setBalance_amount(1000);
        balanceInformationDTO.setPhone_number("1234567890");

        User mockUser = new User();
        mockUser.setIdemail("john.doe@example.com");

        Mockito.when(userRepository.getReferenceById(balanceInformationDTO.getUser()))
                .thenReturn(mockUser);

        // Call the method under test
        Assertions.assertDoesNotThrow(() -> balanceInformationService.saveBalanceInformation(balanceInformationDTO));

        // Verify the expected interactions
        Mockito.verify(userRepository).getReferenceById(balanceInformationDTO.getUser());
        Mockito.verify(balanceInformationRepository).save(Mockito.any(BalanceInformation.class));
    }
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    void increaseBalanceCommited(){
        User mockUser = new User();

        int current_amount  = 1000;
        int bonus_amount = 1000;
        int total_amount = current_amount + bonus_amount;

        mockUser.setIdemail("john.doe@example.com");
        mockUser.setUsername("johndoe");
        BalanceInformation balanceInformation = new BalanceInformation();
        balanceInformation.setId("12345");
        balanceInformation.setUser(mockUser);
        balanceInformation.setBalance_amount(current_amount);
        balanceInformation.setPhone_number("1234567890");

        Mockito.when(userRepository.findByUsername(mockUser.getUsername())).
                thenReturn(Optional.of(mockUser));
        Mockito.when(balanceInformationRepository.findBalanceInformationByUser(mockUser)).
                thenReturn(Optional.of(balanceInformation));

        Optional<BalanceInformationDTO> balanceInformationDTO = balanceInformationService.IncreaseBalance(mockUser.getUsername(),bonus_amount);
        Mockito.verify(userRepository).findByUsername(mockUser.getUsername());
        Mockito.verify(balanceInformationRepository).findBalanceInformationByUser(mockUser);
        Mockito.verify(balanceInformationRepository).updateBalanceWithID(Mockito.any(String.class),Mockito.any(Integer.class));
        balanceInformation.setBalance_amount(total_amount);

        assertEquals(balanceInformation.getBalance_amount(),balanceInformationDTO.get().getBalance_amount());
    }
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    void DecreaseBalanceCommited(){
        User mockUser = new User();

        int current_amount  = 100000;
        int bonus_amount = 1000;
        int total_amount = current_amount - bonus_amount;

        mockUser.setIdemail("john.doe@example.com");
        mockUser.setUsername("johndoe");
        BalanceInformation balanceInformation = new BalanceInformation();
        balanceInformation.setId("12345");
        balanceInformation.setUser(mockUser);
        balanceInformation.setBalance_amount(current_amount);
        balanceInformation.setPhone_number("1234567890");

        Mockito.when(userRepository.findByUsername(mockUser.getUsername())).
                thenReturn(Optional.of(mockUser));
        Mockito.when(balanceInformationRepository.findBalanceInformationByUser(mockUser)).
                thenReturn(Optional.of(balanceInformation));

        Optional<BalanceInformationDTO> balanceInformationDTO = balanceInformationService.DecreaseBalance(mockUser.getUsername(),bonus_amount);
        Mockito.verify(userRepository).findByUsername(mockUser.getUsername());
        Mockito.verify(balanceInformationRepository).findBalanceInformationByUser(mockUser);
        Mockito.verify(balanceInformationRepository).updateBalanceWithID(Mockito.any(String.class),Mockito.any(Integer.class));
        balanceInformation.setBalance_amount(total_amount);

        assertEquals(balanceInformation.getBalance_amount(),balanceInformationDTO.get().getBalance_amount());
    }
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    void increaseBalanceRollBack(){
        User mockUser = new User();

        int current_amount  = 1000;
        int bonus_amount = 1000;

        mockUser.setIdemail("john.doe@example.com");
        mockUser.setUsername("johndoe");
        BalanceInformation balanceInformation = new BalanceInformation();
        balanceInformation.setId("12345");
        balanceInformation.setUser(mockUser);
        balanceInformation.setBalance_amount(current_amount);
        balanceInformation.setPhone_number("1234567890");

        Mockito.when(userRepository.findByUsername(mockUser.getUsername())).
                thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> balanceInformationService.IncreaseBalance(mockUser.getUsername(), bonus_amount));

    }
    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    void decreaseBalanceRollBack(){
        User mockUser = new User();

        int current_amount  = 1000;
        int bonus_amount = 1000;

        mockUser.setIdemail("john.doe@example.com");
        mockUser.setUsername("johndoe");
        BalanceInformation balanceInformation = new BalanceInformation();
        balanceInformation.setId("12345");
        balanceInformation.setUser(mockUser);
        balanceInformation.setBalance_amount(current_amount);
        balanceInformation.setPhone_number("1234567890");

        Mockito.when(userRepository.findByUsername(mockUser.getUsername())).
                thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> balanceInformationService.IncreaseBalance(mockUser.getUsername(), bonus_amount));

    }
    @Test
    void insufficientBalance(){
        User mockUser = new User();

        int current_amount  = 0;
        int bonus_amount = 1000;

        mockUser.setIdemail("john.doe@example.com");
        mockUser.setUsername("johndoe");
        BalanceInformation balanceInformation = new BalanceInformation();
        balanceInformation.setId("12345");
        balanceInformation.setUser(mockUser);
        balanceInformation.setBalance_amount(current_amount);
        balanceInformation.setPhone_number("1234567890");

        Mockito.when(userRepository.findByUsername(mockUser.getUsername())).
                thenReturn(Optional.of(mockUser));
        Mockito.when(balanceInformationRepository.findBalanceInformationByUser(mockUser)).
                thenReturn(Optional.of(balanceInformation));


        assertThrows(InsufficientBalanceException.class, () -> balanceInformationService.DecreaseBalance(mockUser.getUsername(), bonus_amount));

    }

}