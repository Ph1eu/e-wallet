package com.project.Controller;

import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Repository.BalanaceInformationRepository;
import com.project.Service.BalanceInformationService;
import com.project.Service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    BalanceInformationService balanceInformationService;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @GetMapping("/{username}/balance")
    public ResponseEntity<?> getOnlineBalance(@PathVariable("username") String username){

        BalanceInformationDTO balanceInformationDTO=  balanceInformationService.getUserBalanceInformation(username);
        return ResponseEntity.ok().body(balanceInformationDTO);
    }
}
