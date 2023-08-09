//package com.project.Unit.Controller;
//
//import com.project.Assembler.UserResourceAssembler;
//import com.project.Configuration.jwt.JwtServices;
//import com.project.Ultils.MockUserGenerator;
//import com.project.Controller.UserController;
//import com.project.Model.User;
//import com.project.Payload.DTO.UserDTO;
//import com.project.Payload.Response.MessageResponse;
//import com.project.Repository.RoleRepository;
//import com.project.Service.AddressService;
//import com.project.Service.CustomUserDetail;
//import com.project.Service.PaymentCardsService;
//import com.project.Service.UserDetailServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Import;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//@WebMvcTest(UserController.class)
//@ComponentScan(basePackages = "com.project")
//@Import({ UserResourceAssembler.class })
//
//class UserControllerTest {
//
//    @MockBean
//    private UserDetailServiceImpl userDetailService;
//
//    @MockBean
//    private PaymentCardsService paymentCardsService;
//
//    @MockBean
//    private AddressService addressService;
//
//    @MockBean
//    private JwtServices jwtServices;
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private UserResourceAssembler userResourceAssembler;
//    @MockBean
//    private RoleRepository roleRepository;
//    @InjectMocks
//    private UserController userController;
//    private MockUserGenerator mockUserGenerator;
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//    public UserDTO mockUserInstance(UserDTO mockUser){
//        CustomUserDetail customUserDetail = CustomUserDetail.build(new User(mockUser));
//
//        Authentication authentication = Mockito.mock(Authentication.class);
//        Mockito.when(authentication.getPrincipal()).thenReturn(customUserDetail);
//        SecurityContext securityContext = mock(SecurityContext.class);
//        SecurityContextHolder.setContext(securityContext);
//
//        // Set up the authentication and user details in the security context holder
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.getPrincipal()).thenReturn(customUserDetail);
//        when(userDetailService.loadUserByUsername(mockUser.getUsername())).thenReturn(customUserDetail);
//
//        // Call the method to be tested
//        UserDTO result = userController.verifyUserInstance(mockUser.getUsername());
//        return result;
//    }
//
//
//
//
//
//
//
//    @Test
//    void verifyUserInstance() {
//        // Prepare test data
//        UserDTO user =  mockUserGenerator.generateMockUserDTO(100);
//
//        UserDTO result = mockUserInstance(user);
//
//        // Assert the result
//        assertNotNull(result);
//        assertTrue(user.equals(result));
//
//    }
//
//    @Test
//    void validateDateString() {
//    }
//
//    @Test
//    void getOneUser()throws Exception  {
//        // Prepare test data
//        UserDTO user =  mockUserGenerator.generateMockUserDTO(50);
//        // Create a mock UserDetails object
//
//        UserDTO result = mockUserInstance(user);
//
//        EntityModel<UserDTO> entityModel = userResourceAssembler.toModel(result);
//
//        ResponseEntity<?> response = userController.getOneUser(result.getUsername());
//        System.out.println(entityModel);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(entityModel, response.getBody());
//
//        // Test case: User not found
//        when(userController.verifyUserInstance("someusername")).thenReturn(null);
//
//        ResponseEntity<?> errorResponse = userController.getOneUser(result.getUsername());
//
//        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
//        assertEquals(new MessageResponse("Invalid User"), errorResponse.getBody());
//    }
//
//    @Test
//    void getAddress() {
//        UserDTO user =  mockUserGenerator.generateMockUserDTO(100);
//        UserDTO result = mockUserInstance(user);
//
//
//    }
//
//    @Test
//    void setAddress() {
//    }
//
//    @Test
//    void deleteAddress() {
//    }
//
//    @Test
//    void getPaymentCards() {
//    }
//
//    @Test
//    void setPaymentCards() {
//    }
//
//    @Test
//    void deletePaymentCardbyID() {
//    }
//
//    @Test
//    void deleteAllPaymentCard() {
//    }
//}