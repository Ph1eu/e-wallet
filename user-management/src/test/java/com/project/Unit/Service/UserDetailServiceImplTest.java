package com.project.Unit.Service;

import com.project.Model.BalanceInformation;
import com.project.Model.Paymentcard;
import com.project.Model.User;
import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Payload.DTO.PaymentcardDTO;
import com.project.Payload.DTO.UserDTO;
import com.project.Repository.BalanceInformationRepository;
import com.project.Repository.UserRepository;
import com.project.Service.UserDetailServiceImpl;
import com.project.Ultils.MockUserGenerator;
import com.project.Ultils.ObjectComparator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BalanceInformationRepository balanceInformationRepository;
    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void getUserWithBalanceInformation() {
        // Setting up mock user data
        MockUserGenerator mockUserGenerator = new MockUserGenerator();
        User user =  mockUserGenerator.generateMockUser(10);
        String user_id = "user1@example.com";
        user.setIdemail(user_id);

        // setting mock balance information
        BalanceInformation balanceInformation = mockUserGenerator.generateBalanceInformation();
        balanceInformation.setUser("user1@example.com");

        //setting payment cards information
        List<Paymentcard> paymentcards = mockUserGenerator.generateMockPaymentCards(2);
        user.setPaymentcards(paymentcards);

        UserDTO userDTO = new UserDTO(user);
        userDTO.setBalanceInformation(new BalanceInformationDTO(balanceInformation));
        List<PaymentcardDTO> paymentcardDTOS = new ArrayList<>();
        for (Paymentcard paymentcard:user.getPaymentcards()){
            paymentcard.setUser(user);
            paymentcardDTOS.add(new PaymentcardDTO(paymentcard));
        }
        userDTO.setPaymentcards(paymentcardDTOS);
        for(PaymentcardDTO paymentcard:userDTO.getPaymentcards()){
            paymentcard.setUser(userDTO);
        }
        Mockito.when(userRepository.findById(user_id)).thenReturn(Optional.of(user));
        Mockito.when(balanceInformationRepository.findBalanceInformationsByUserId(user_id)).thenReturn(Optional.of(balanceInformation));

        // call method under test
        UserDTO result = userDetailService.getUserWithBalanceInformation(user_id);

        Mockito.verify(userRepository).findById(user_id);
        Mockito.verify(balanceInformationRepository).findBalanceInformationsByUserId(user_id);

        assertTrue(ObjectComparator.comparePaymentcardsWithDTO(user.getPaymentcards(),result.getPaymentcards()));

    }

    @Test
    void getAllUsersWithEmailandBalance() {
        int object_count = 30;
        int size = 6;
        int num_page = (int) Math.ceil((double) object_count / size);

        Integer balance = 100000000;
        ObjectComparator objectComparator = new ObjectComparator();
        // Setting up mock user data
        MockUserGenerator mockUserGenerator = new MockUserGenerator();
        List<User> users =  mockUserGenerator.generateMockUsers(object_count);
        String user_id = "user1@example.com";

        for (int i = 0;i < object_count;i++){
            User user = users.get(i);
            user.setId_email(user_id);
            List<Paymentcard> paymentcards = mockUserGenerator.generateMockPaymentCards(2);
            for (Paymentcard paymentcard:paymentcards){
                paymentcard.setUser(user);
            }
            user.setPaymentcards(paymentcards);

        }
        // setting mock balance information
        List<BalanceInformation> balanceInformations = mockUserGenerator.generateMockBalanceInformations(object_count);
        for (int i = 0;i < object_count;i++){
            User user = users.get(i);
            BalanceInformation balanceInformation = balanceInformations.get(i);
            balanceInformation.setUser(user.getIdemail());
        }

        // initial object list
        List<Object[]> resultList = new ArrayList<>();
        for (int i = 0;i < object_count;i++){
            User user = users.get(i);
            BalanceInformation balanceInformation = balanceInformations.get(i);
            resultList.add(new Object[]{user, balanceInformation});
        }
        for (int i = 0 ; i < num_page;i++){
            Pageable pageable = PageRequest.of(i, size);

            Page<Object[]> resultPage = new PageImpl<>(resultList, pageable, object_count);
            Mockito.when(userRepository.findUserWithEmailandBalance(user_id, balance, pageable)).thenReturn(resultPage);
            // call method under test
            Page<UserDTO> result = userDetailService.getAllUsersWithEmailandBalance(user_id, balance, pageable);

            Mockito.verify(userRepository).findUserWithEmailandBalance(user_id, balance, pageable);
            assertEquals(object_count, result.getContent().size());
            for (int j = 0 ; j < size;j++){
                Object[] row = resultPage.getContent().get(i);
                User user = (User) row[0];
                BalanceInformation balanceInformation = (BalanceInformation) row[1];
                UserDTO userDTO  = result.getContent().get(i);
                assertEquals(user_id,userDTO.getId_email());
                assertEquals(balance.intValue(),userDTO.getBalanceInformation().getBalance_amount());
                assertTrue(ObjectComparator.compareUserWithDTO(user,userDTO));
                assertTrue(ObjectComparator.compareBalanceInformationWithDTO(balanceInformation,userDTO.getBalanceInformation()));
                assertTrue(ObjectComparator.comparePaymentcardsWithDTO(user.getPaymentcards(),userDTO.getPaymentcards()));

            }
        }


    }

    @Test
    void getAllUsersWithEmail() {
        int object_count = 30;
        int size = 6;
        int num_page = (int) Math.ceil((double) object_count / size);

        // Setting up mock user data
        MockUserGenerator mockUserGenerator = new MockUserGenerator();
        List<User> users =  mockUserGenerator.generateMockUsers(object_count);
        String user_id = "user1@example.com";
        for (int i = 0;i < object_count;i++){
            User user = users.get(i);
            user.setId_email(user_id);
            List<Paymentcard> paymentcards = mockUserGenerator.generateMockPaymentCards(2);
            for (Paymentcard paymentcard:paymentcards){
                paymentcard.setUser(user);
            }
            user.setPaymentcards(paymentcards);
        }
        // setting mock balance information
        List<BalanceInformation> balanceInformations = mockUserGenerator.generateMockBalanceInformations(object_count);
        for (int i = 0;i < object_count;i++){
            User user = users.get(i);
            BalanceInformation balanceInformation = balanceInformations.get(i);
            balanceInformation.setUser(user.getIdemail());
        }
        // initial object list
        List<Object[]> resultList = new ArrayList<>();
        for (int i = 0;i < object_count;i++){
            User user = users.get(i);
            BalanceInformation balanceInformation = balanceInformations.get(i);
            resultList.add(new Object[]{user, balanceInformation});
        }
        for (int i = 0 ; i < num_page;i++){
            Pageable pageable = PageRequest.of(i, size);
        Page<Object[]> resultPage = new PageImpl<>(resultList, pageable, object_count);
        Mockito.when(userRepository.findUserWithEmail(user_id, pageable)).thenReturn(resultPage);
        // call method under test
        Page<UserDTO> result = userDetailService.getAllUsersWithEmail(user_id, pageable);

        Mockito.verify(userRepository).findUserWithEmail(user_id, pageable);
        assertEquals(object_count, result.getContent().size());
        assertEquals(size,result.getSize());
        for (int j = 0 ; j < size;j++){
            Object[] row = resultPage.getContent().get(i);
            User user = (User) row[0];
            BalanceInformation balanceInformation = (BalanceInformation) row[1];
            UserDTO userDTO  = result.getContent().get(i);
            assertEquals(user_id,userDTO.getId_email());
            assertTrue(ObjectComparator.compareUserWithDTO(user,userDTO));
            assertTrue(ObjectComparator.compareBalanceInformationWithDTO(balanceInformation,userDTO.getBalanceInformation()));
            assertTrue(ObjectComparator.comparePaymentcardsWithDTO(user.getPaymentcards(),userDTO.getPaymentcards()));

        }
        }
    }

    @Test
    void getAllUsersWithBalance() {
        int object_count = 30;
        int size = 6;
        int num_page = (int) Math.ceil((double) object_count / size);
        Integer balance = 100000000;

        // Setting up mock user data
        MockUserGenerator mockUserGenerator = new MockUserGenerator();
        List<User> users =  mockUserGenerator.generateMockUsers(object_count);
        // setting mock balance information
        List<BalanceInformation> balanceInformations = mockUserGenerator.generateMockBalanceInformations(object_count);
        for (int i = 0;i < object_count;i++){
            User user = users.get(i);
            BalanceInformation balanceInformation = balanceInformations.get(i);
            balanceInformation.setUser(user.getIdemail());
            List<Paymentcard> paymentcards = mockUserGenerator.generateMockPaymentCards(2);
            for (Paymentcard paymentcard:paymentcards){
                paymentcard.setUser(user);
            }
            user.setPaymentcards(paymentcards);
        }
        // initial object list
        List<Object[]> resultList = new ArrayList<>();
        for (int i = 0;i < object_count;i++){
            User user = users.get(i);
            BalanceInformation balanceInformation = balanceInformations.get(i);
            resultList.add(new Object[]{user, balanceInformation});
        }
        for (int i = 0 ; i < num_page;i++){
            Pageable pageable = PageRequest.of(i, size);
        Page<Object[]> resultPage = new PageImpl<>(resultList, pageable, 30);
        Mockito.when(userRepository.findUserWithBalance( balance, pageable)).thenReturn(resultPage);
        // call method under test
        Page<UserDTO> result = userDetailService.getAllUsersWithBalance(balance, pageable);

        Mockito.verify(userRepository).findUserWithBalance( balance, pageable);
        assertEquals(object_count, result.getContent().size());
        for (int j = 0 ; j < size;j++){
                Object[] row = resultPage.getContent().get(i);
                User user = (User) row[0];
                BalanceInformation balanceInformation = (BalanceInformation) row[1];
                UserDTO userDTO  = result.getContent().get(i);
                assertEquals(balance.intValue(),userDTO.getBalanceInformation().getBalance_amount());
                assertTrue(ObjectComparator.compareUserWithDTO(user,userDTO));
                assertTrue(ObjectComparator.compareBalanceInformationWithDTO(balanceInformation,userDTO.getBalanceInformation()));
                assertTrue(ObjectComparator.comparePaymentcardsWithDTO(user.getPaymentcards(),userDTO.getPaymentcards()));

        }
        }
    }

    @Test
    void getAllUsersWithBalanceInformation() {
        int object_count = 30;
        int size = 6;
        int num_page = (int) Math.ceil((double) object_count / size);


        // Setting up mock user data
        MockUserGenerator mockUserGenerator = new MockUserGenerator();
        List<User> users =  mockUserGenerator.generateMockUsers(object_count);

        // setting mock balance information
        List<BalanceInformation> balanceInformations = mockUserGenerator.generateMockBalanceInformations(object_count);
        for (int i = 0;i < object_count;i++){
            User user = users.get(i);
            BalanceInformation balanceInformation = balanceInformations.get(i);
            balanceInformation.setUser(user.getIdemail());
            List<Paymentcard> paymentcards = mockUserGenerator.generateMockPaymentCards(2);
            for (Paymentcard paymentcard:paymentcards){
                paymentcard.setUser(user);
            }
            user.setPaymentcards(paymentcards);
        }
        // initial object list
        List<Object[]> resultList = new ArrayList<>();
        for (int i = 0;i < object_count;i++){
            User user = users.get(i);
            BalanceInformation balanceInformation = balanceInformations.get(i);
            resultList.add(new Object[]{user, balanceInformation});
        }
        for (int i = 0 ; i < num_page;i++){
            Pageable pageable = PageRequest.of(i, size);
        Page<Object[]> resultPage = new PageImpl<>(resultList, pageable, 30);
        Mockito.when(userRepository.findAllUsersWithBalanceInformation( pageable)).thenReturn(resultPage);
        // call method under test
        Page<UserDTO> result = userDetailService.getAllUsersWithBalanceInformation( pageable);

        Mockito.verify(userRepository).findAllUsersWithBalanceInformation( pageable);
        assertEquals(object_count, result.getContent().size());
            for (int j = 0 ; j < size;j++){
                Object[] row = resultPage.getContent().get(i);
                User user = (User) row[0];
                BalanceInformation balanceInformation = (BalanceInformation) row[1];
                UserDTO userDTO  = result.getContent().get(i);
                assertTrue(ObjectComparator.compareUserWithDTO(user,userDTO));
                assertTrue(ObjectComparator.compareBalanceInformationWithDTO(balanceInformation,userDTO.getBalanceInformation()));
                assertTrue(ObjectComparator.comparePaymentcardsWithDTO(user.getPaymentcards(),userDTO.getPaymentcards()));

            }
    }
    }
}