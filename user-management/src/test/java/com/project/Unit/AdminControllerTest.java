//package com.project.Unit;
//
//import com.project.Assembler.UserResourceAssembler;
//import com.project.Controller.AdminController;
//import com.project.Model.ERole;
//import com.project.Payload.DTO.RoleDTO;
//import com.project.Payload.DTO.UserDTO;
//import com.project.Service.UserDetailServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.hateoas.CollectionModel;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class AdminControllerTest {
//    @InjectMocks
//    private AdminController adminController;
//    @Mock
//    private UserDetailServiceImpl userDetailService;
//    @Mock
//    private UserResourceAssembler userResourceAssembler;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllUsers() {
//       //  Mocking the user data
//        List<UserDTO> mockUsers = new ArrayList<>();
//        mockUsers.add(new UserDTO("hieupm.bi11-090@st.usth.edu.vn",
//                                "hieuhg12345","123456789",
//                                "Hieu","Pham",new Date(),new RoleDTO(ERole.ROLE_USER),null,null));
//        mockUsers.add(new UserDTO("banyamanohg@gmail.com",
//                "hieuhg123457890","1234567890",
//                "Hieu","Pham",new Date(),new RoleDTO(ERole.ROLE_ADMIN),null,null));
//
//        // Test case: Users found
//        when(userDetailService.findAll()).thenReturn(mockUsers);
//        UserCollectionModel models=  new UserCollectionModel(mockUsers);
//
//        CollectionModel<EntityModel<UserDTO>> collectionModel = userResourceAssembler.toCollectionModel(mockUsers);
//
//        when(userResourceAssembler.toCollectionModel(mockUsers)).thenReturn(collectionModel);
//
//        ResponseEntity<?> response = adminController.getAllUsers();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(collectionModel, response.getBody());
//
//        // Test case: Users not found
//        when(userDetailService.findAll()).thenReturn(null);
//
//        response = adminController.getAllUsers();
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals(null, response.getBody());
//    }
//}
//class UserCollectionModel extends CollectionModel<UserDTO> {
//
//    public UserCollectionModel(Iterable<UserDTO> content) {
//        super(content);
//    }
//}