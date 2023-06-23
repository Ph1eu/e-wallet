package com.project.Unit.Service;

import com.project.Model.BalanceInformation;
import com.project.Model.User;
import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Repository.BalanceInformationRepository;
import com.project.Repository.UserRepository;
import com.project.Service.BalanceInformationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
                .thenReturn(mockBalanceInformation);
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
                .thenReturn(mockBalanceInformation);

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
}