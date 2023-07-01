package com.project.Ultils;


import com.project.Payload.DTO.UserDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Component
public class MockUserGenerator {
    public List<UserDTO> generateMockUsers(int count) {
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

        UserDTO user = new UserDTO(email, username, password, firstName, lastName, registrationDate, null, null);

        user.setPaymentcardsDTO(null);
        return user;
    }




}
