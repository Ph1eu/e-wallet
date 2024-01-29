package com.project.ultils;

import com.project.model.*;
import com.project.payload.dto.*;

import java.util.*;

public class MockUserGenerator {
    public List<UserDTO> generateMockUserDTOs(int count) {
        List<UserDTO> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            UserDTO user = generateMockUserDTO(i);
            users.add(user);
        }

        return users;
    }
    public List<User> generateMockUsers(int count) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            User user = generateMockUser(i);
            users.add(user);
        }

        return users;
    }
    public List<BalanceInformation> generateMockBalanceInformations(int count) {
        List<BalanceInformation> balanceInformationList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            BalanceInformation balanceInformation = generateBalanceInformation();
            balanceInformationList.add(balanceInformation);
        }

        return balanceInformationList;
    }

    public  UserDTO generateMockUserDTO(int index) {
        String email = "user" + index + "@example.com";
        String username = "user" + index;
        String password = "password" + index;
        String firstName = "First" + index;
        String lastName = "Last" + index;
        Date registrationDate = new Date();
        RoleDTO role = new RoleDTO(ERole.ROLE_USER);
        AddressDTO address = generateMockAddressDTO(index);
        UserDTO user = new UserDTO(email, username, password, firstName, lastName, registrationDate, role, address, null,null);
        List<PaymentcardDTO> paymentCards = generateMockPaymentCardsDTO(index);
        for (PaymentcardDTO paymentcardDTO:paymentCards){
            paymentcardDTO.setUser(user);

        }
        user.setPaymentcardsDTO(paymentCards);
        BalanceInformationDTO balanceInformationDTO = generateBalanceInformationDTO();
        balanceInformationDTO.setUser(user.getIdemail());
        user.setBalanceInformation(balanceInformationDTO);
        return user;
    }
    public  User generateMockUser(int index) {
        String email = "user" + index + "@example.com";
        String username = "user" + index;
        String password = "password" + index;
        String firstName = "First" + index;
        String lastName = "Last" + index;
        Date registrationDate = new Date();
        Role role = new Role(ERole.ROLE_USER);
        Address address = generateMockAddress(index);
        User user = new User(email, username, password, firstName, lastName, registrationDate, role, address, null);

        return user;
    }
    public  AddressDTO generateMockAddressDTO(int index) {
        return new AddressDTO("Street" + index, "City" + index, "Province" + index, "Country" + index);
    }
    public  Address generateMockAddress(int index) {
        return new Address("Street" + index, "City" + index, "Province" + index, "Country" + index);
    }
    public BalanceInformationDTO generateBalanceInformationDTO(){
        String id = UUID.randomUUID().toString();
        int balance_amount = 100000000;
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            int randomNumber = random.nextInt(10); // Generate a random digit between 0 and 9
            stringBuilder.append(randomNumber);
        }

        String phone_number = stringBuilder.toString();
        return new BalanceInformationDTO(id,null,balance_amount,phone_number);
    }
    public BalanceInformation generateBalanceInformation(){
        String id = UUID.randomUUID().toString();
        int balance_amount = 100000000;
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 9; i++) {
            int randomNumber = random.nextInt(10); // Generate a random digit between 0 and 9
            stringBuilder.append(randomNumber);
        }

        String phone_number = stringBuilder.toString();
        return new BalanceInformation(id,null,balance_amount,phone_number);
    }
    public  List<PaymentcardDTO> generateMockPaymentCardsDTO(int index) {
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
    public  List<Paymentcard> generateMockPaymentCards(int index) {
        List<Paymentcard> paymentCards = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String id = UUID.randomUUID().toString();
            String cardNumber = "1234-5678-90" + i;
            User user = null;  // Set the user reference accordingly
            String cardHolderName = "Card Holder" + i;
            String cardType = "Card Type" + i;
            Date registrationDate = new Date();


            Date expirationDate = new Date() ;

            Paymentcard paymentCard = new Paymentcard(id, cardNumber, user, cardHolderName, cardType, registrationDate, expirationDate);
            paymentCards.add(paymentCard);
        }

        return paymentCards;

    }

}
