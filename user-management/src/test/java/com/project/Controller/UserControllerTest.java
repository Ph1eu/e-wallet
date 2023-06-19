package com.project.Controller;

import com.project.Assembler.UserResourceAssembler;
import com.project.Configuration.jwt.JwtServices;
import com.project.Model.ERole;
import com.project.Model.User;
import com.project.Payload.DTO.AddressDTO;
import com.project.Payload.DTO.PaymentcardDTO;
import com.project.Payload.DTO.RoleDTO;
import com.project.Payload.DTO.UserDTO;
import com.project.Payload.Response.MessageResponse;
import com.project.Repository.RoleRepository;
import com.project.Service.AddressService;
import com.project.Service.CustomUserDetail;
import com.project.Service.PaymentCardsService;
import com.project.Service.UserDetailServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@WebMvcTest(UserController.class)
@ComponentScan(basePackages = "com.project")

class UserControllerTest {

    @MockBean
    private UserDetailServiceImpl userDetailService;

    @MockBean
    private PaymentCardsService paymentCardsService;

    @MockBean
    private AddressService addressService;

    @MockBean
    private JwtServices jwtServices;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserResourceAssembler userResourceAssembler;
    @MockBean
    private RoleRepository roleRepository;
    @InjectMocks
    private UserController userController;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    public UserDTO mockUserInstance(UserDTO mockUser){
        CustomUserDetail customUserDetail = CustomUserDetail.build(new User(mockUser));

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(customUserDetail);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Set up the authentication and user details in the security context holder
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(customUserDetail);
        when(userDetailService.loadUserByUsername(mockUser.getUsername())).thenReturn(customUserDetail);

        // Call the method to be tested
        UserDTO result = userController.verifyUserInstance(mockUser.getUsername());
        return result;
    }
    public  List<UserDTO> generateMockUsers(int count) {
        List<UserDTO> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            UserDTO user = generateMockUser(i);
            users.add(user);
        }

        return users;
    }

    public  UserDTO generateMockUser(int index) {
        String email = "user" + index + "@example.com";
        String username = "user" + index;
        String password = "password" + index;
        String firstName = "First" + index;
        String lastName = "Last" + index;
        Date registrationDate = new Date();
        RoleDTO role = new RoleDTO(ERole.ROLE_USER);
        AddressDTO address = generateMockAddress(index);
        UserDTO user = new UserDTO(email, username, password, firstName, lastName, registrationDate, role, address, null);
        List<PaymentcardDTO> paymentCards = generateMockPaymentCards(index);
        for (PaymentcardDTO paymentcardDTO:paymentCards){
            paymentcardDTO.setUser(user);

        }
        user.setPaymentcardsDTO(paymentCards);
        return user;
    }
    public  AddressDTO generateMockAddress(int index) {
        return new AddressDTO("Street" + index, "City" + index, "Province" + index, "Country" + index);
    }

    public  List<PaymentcardDTO> generateMockPaymentCards(int index) {
        List<PaymentcardDTO> paymentCards = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String id = UUID.randomUUID().toString();
            String cardNumber = "1234-5678-90" + i;
            UserDTO user = null;  // Set the user reference accordingly
            String cardHolderName = "Card Holder" + i;
            String cardType = "Card Type" + i;
            Date registrationDate = new Date();


            Date expirationDate = new Date() ;

            PaymentcardDTO paymentCard = new PaymentcardDTO(id, cardNumber, user, cardHolderName, cardType, registrationDate, expirationDate);
            paymentCards.add(paymentCard);
        }

        return paymentCards;

    }




    @Test
    void verifyUserInstance() {
        // Prepare test data
        UserDTO user =  generateMockUser(100);

        UserDTO result = mockUserInstance(user);

        // Assert the result
        assertNotNull(result);
        assertTrue(user.equals(result));

    }

    @Test
    void validateDateString() {
    }

    @Test
    void getOneUser()throws Exception  {
        // Prepare test data
        UserDTO user =  generateMockUser(50);
        // Create a mock UserDetails object

        UserDTO result = mockUserInstance(user);

        EntityModel<UserDTO> entityModel = userResourceAssembler.toModel(result);

        ResponseEntity<?> response = userController.getOneUser(result.getUsername());
        System.out.println(entityModel);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(entityModel, response.getBody());

        // Test case: User not found
        when(userController.verifyUserInstance("someusername")).thenReturn(null);

        ResponseEntity<?> errorResponse = userController.getOneUser(result.getUsername());

        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
        assertEquals(new MessageResponse("Invalid User"), errorResponse.getBody());
    }

    @Test
    void getAddress() {
        UserDTO user =  generateMockUser(100);
        UserDTO result = mockUserInstance(user);


    }

    @Test
    void setAddress() {
    }

    @Test
    void deleteAddress() {
    }

    @Test
    void getPaymentCards() {
    }

    @Test
    void setPaymentCards() {
    }

    @Test
    void deletePaymentCardbyID() {
    }

    @Test
    void deleteAllPaymentCard() {
    }
}