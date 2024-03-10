//package com.project.ultils;
//
//import com.project.service.balanceinformation.entity.BalanceInformation;
//import com.project.service.paymentcard.entity.Paymentcard;
//import com.project.service.user.entity.User;
//import com.project.service.balanceinformation.dto.BalanceInformationDTO;
//import com.project.service.paymentcard.dto.PaymentcardDTO;
//import com.project.service.user.dto.UserDTO;
//
//import java.util.List;
//import java.util.Objects;
//
//public class ObjectComparator {
//    public static boolean compareUserWithDTO(User user, UserDTO dto) {
//
//        if (user == null || dto == null) {
//            System.out.println("object is null");
//
//            return false;
//        }
//        boolean match = true;
//
//        if (!Objects.equals(user.getIdemail(), dto.getIdemail())) {
//            System.out.println("ID email mismatch: " + user.getIdemail() + " != " + dto.getIdemail());
//            match = false;
//        }
//        if (!Objects.equals(user.getUsername(), dto.getUsername())) {
//            System.out.println("Username mismatch: " + user.getUsername() + " != " + dto.getUsername());
//            match = false;
//        }
//        if (!Objects.equals(user.getPassword(), dto.getPassword())) {
//            System.out.println("Password mismatch: " + user.getPassword() + " != " + dto.getPassword());
//            match = false;
//        }
//        if (!Objects.equals(user.getFirst_name(), dto.getFirst_name())) {
//            System.out.println("First name mismatch: " + user.getFirst_name() + " != " + dto.getFirst_name());
//            match = false;
//        }
//        if (!Objects.equals(user.getLast_name(), dto.getLast_name())) {
//            System.out.println("Last name mismatch: " + user.getLast_name() + " != " + dto.getLast_name());
//            match = false;
//        }
//        if (!Objects.equals(user.getRegistration_date(), dto.getRegistration_date())) {
//            System.out.println("Registration date mismatch: " + user.getRegistration_date() + " != " + dto.getRegistration_date());
//            match = false;
//        }
//        if (!Objects.equals(user.getRoles().getName(), dto.getRoles().getName())) {
//            System.out.println("Roles mismatch: " + user.getRoles().getName() + " != " + dto.getRoles().getName());
//            match = false;
//        }
//
//        return match;
//    }
//
//    public static boolean compareBalanceInformationWithDTO(BalanceInformation balanceInfo, BalanceInformationDTO dto) {
//
//        if (balanceInfo == null || dto == null) {
//            return false;
//        }
//
//        boolean match = true;
//
//        if (!Objects.equals(balanceInfo.getId(), dto.getId())) {
//            System.out.println("ID mismatch: " + balanceInfo.getId() + " != " + dto.getId());
//            match = false;
//        }
//        if (!Objects.equals(balanceInfo.getUser(), dto.getUser())) {
//            System.out.println("User mismatch");
//            match = false;
//        }
//        if (balanceInfo.getBalance_amount() != dto.getBalance_amount()) {
//            System.out.println("Balance amount mismatch: " + balanceInfo.getBalance_amount() + " != " + dto.getBalance_amount());
//            match = false;
//        }
//        if (!Objects.equals(balanceInfo.getPhone_number(), dto.getPhone_number())) {
//            System.out.println("Phone number mismatch: " + balanceInfo.getPhone_number() + " != " + dto.getPhone_number());
//            match = false;
//        }
//
//        return match;
//    }
//
//    public static boolean comparePaymentcardWithDTO(Paymentcard paymentcard, PaymentcardDTO dto) {
//
//        if (paymentcard == null || dto == null) {
//            return false;
//        }
//        boolean match = true;
//
//        if (!Objects.equals(paymentcard.getId(), dto.getId())) {
//            System.out.println("ID mismatch: " + paymentcard.getId() + " != " + dto.getId());
//            match = false;
//        }
//        if (!Objects.equals(paymentcard.getCard_number(), dto.getCard_number())) {
//            System.out.println("Card number mismatch: " + paymentcard.getCard_number() + " != " + dto.getCard_number());
//            match = false;
//        }
//        if (!compareUserWithDTO(paymentcard.getUser(), dto.getUser())) {
//            System.out.println("User mismatch");
//            match = false;
//        }
//        if (!Objects.equals(paymentcard.getCard_holder_name(), dto.getCard_holder_name())) {
//            System.out.println("Card holder name mismatch: " + paymentcard.getCard_holder_name() + " != " + dto.getCard_holder_name());
//            match = false;
//        }
//        if (!Objects.equals(paymentcard.getCard_type(), dto.getCard_type())) {
//            System.out.println("Card type mismatch: " + paymentcard.getCard_type() + " != " + dto.getCard_type());
//            match = false;
//        }
//        if (!Objects.equals(paymentcard.getRegistration_date(), dto.getRegistration_date())) {
//            System.out.println("Registration date mismatch: " + paymentcard.getRegistration_date() + " != " + dto.getRegistration_date());
//            match = false;
//        }
//        if (!Objects.equals(paymentcard.getExpiration_date(), dto.getExpiration_date())) {
//            System.out.println("Expiration date mismatch: " + paymentcard.getExpiration_date() + " != " + dto.getExpiration_date());
//            match = false;
//        }
//
//        return match;
//    }
//
//    public static boolean comparePaymentcardsWithDTO(List<Paymentcard> paymentcards, List<PaymentcardDTO> dtos) {
//
//        if (paymentcards == null || dtos == null) {
//            System.out.println("object null");
//            return false;
//        }
//        if (paymentcards.size() != dtos.size()) {
//            System.out.println("size doesn't match");
//            return false;
//        }
//        for (int i = 0; i < paymentcards.size(); i++) {
//            Paymentcard paymentcard = paymentcards.get(i);
//            PaymentcardDTO dto = dtos.get(i);
//            if (!comparePaymentcardWithDTO(paymentcard, dto)) {
//                System.out.println("Wrong information");
//                return false;
//            }
//        }
//        return true;
//    }
//}
