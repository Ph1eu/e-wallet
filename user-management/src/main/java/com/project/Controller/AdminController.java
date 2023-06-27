package com.project.Controller;

import com.project.Assembler.UserResourceAssembler;
import com.project.Configuration.jwt.JwtServices;
import com.project.Model.User;
import com.project.Payload.DTO.UserDTO;
import com.project.Repository.UserRepository;
import com.project.Service.CustomUserDetail;
import com.project.Service.UserDetailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    UserResourceAssembler userResourceAssembler;
    private static final Logger logger = LoggerFactory.getLogger(JwtServices.class);


    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@Param("email") String email,@Param("balance") Integer balance ){
        if(email == null  && balance == null){
            List<UserDTO> users = userDetailService.getAllUsersWithBalanceInformation();
            if (users == null){
                return  ResponseEntity.notFound().build();
            }
            else{
                return ResponseEntity.ok().body(userResourceAssembler.toCollectionModelInWrapper(users));
            }
        }
        else if (email != null && balance != null) {
            List<UserDTO> users = userDetailService.getAllUsersWithEmailandBalance(email,balance);
            if (users == null){
                return  ResponseEntity.notFound().build();
            }
            else{
                return ResponseEntity.ok().body(userResourceAssembler.toCollectionModelInWrapper(users));
            }

        }else if (email != null && balance == null) {
            List<UserDTO> users = userDetailService.getAllUsersWithEmail(email);
            if (users == null){
                return  ResponseEntity.notFound().build();
            }
            else{
                return ResponseEntity.ok().body(userResourceAssembler.toCollectionModelInWrapper(users));
            }

        }else if (balance != null && email == null ) {
            List<UserDTO> users = userDetailService.getAllUsersWithBalance(balance);
            if (users == null){
                return  ResponseEntity.notFound().build();
            }
            else{
                return ResponseEntity.ok().body(userResourceAssembler.toCollectionModelInWrapper(users));
            }
        }
        return ResponseEntity.badRequest().build();

    }
}




