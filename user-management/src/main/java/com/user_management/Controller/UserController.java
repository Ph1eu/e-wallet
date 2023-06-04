package com.user_management.Controller;

import com.user_management.Model.User;
import com.user_management.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userRepository.findAll();
        if (users == null){
            return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);
        }
        else{
            return ResponseEntity.ok(users);
        }
    }


}
