package com.project.Controller;

import com.project.Model.ERole;
import com.project.Payload.DTO.AddressDTO;
import com.project.Payload.DTO.PaymentcardDTO;
import com.project.Payload.DTO.RoleDTO;
import com.project.Payload.DTO.UserDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

}
