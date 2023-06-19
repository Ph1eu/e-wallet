package com.project.Controller;

import com.project.Assembler.UserResourceAssembler;
import com.project.Model.User;
import com.project.Payload.DTO.UserDTO;
import com.project.Repository.UserRepository;
import com.project.Service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    UserResourceAssembler userResourceAssembler;
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        List<UserDTO> users = userDetailService.findAll();
        if (users == null){
            return  ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok().body(userResourceAssembler.toCollectionModel(users));
        }
    }



}
