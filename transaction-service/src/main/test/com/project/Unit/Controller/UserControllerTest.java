package com.project.Unit.Controller;

import com.project.Assembler.BalanceResourceAssembler;
import com.project.Controller.UserController;
import com.project.Exceptions.CustomExceptions.BusinessLogic.InsufficientBalanceException;
import com.project.Exceptions.CustomExceptions.BusinessLogic.TransferFailedException;
import com.project.Exceptions.CustomExceptions.BusinessLogic.UserNotFoundException;
import com.project.Exceptions.CustomExceptions.ValidationInput.InvalidPhoneNumberFormatException;
import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Payload.DTO.TransactionHistoryDTO;
import com.project.Payload.Response.ResponseEntityWrapper;
import com.project.Service.BalanceInformationService;
import com.project.Service.TransactionHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


public class UserControllerTest {
    @Mock
    private BalanceInformationService balanceInformationService;

    @Mock
    private TransactionHistoryService transactionHistoryService;
    @Mock
    private BalanceResourceAssembler balanceResourceAssembler;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOnlineBalance() {
    }

    @Test
    void depositMoney() {
    }

    @Test
    void withdrawalMoney() {
    }

    @Test
    void transferMoney() {
    }

    @Test
    void getHistory() {

    }
    @Test
    public void testWithdrawalMoneyException() throws Exception  {
        String username = "senderUsername";
        String amountstr = "100";
        int amount = Integer.parseInt(amountstr);

        when(balanceInformationService.IncreaseBalance(eq(username), eq(amount)))
                .thenThrow(new TransferFailedException("Failed"));
        assertThrows(TransferFailedException.class, () -> userController.depositMoney(username, amountstr));

        when(balanceInformationService.IncreaseBalance(eq(username), eq(amount)))
                .thenThrow(new UserNotFoundException("Failed"));
        assertThrows(UserNotFoundException.class, () -> userController.depositMoney(username, amountstr));

        when(balanceInformationService.IncreaseBalance(eq(username), eq(amount)))
                .thenThrow(new NumberFormatException());
        assertThrows(NumberFormatException.class, () -> userController.depositMoney(username, "amountstr"));

        when(balanceInformationService.IncreaseBalance(eq(username), eq(amount)))
                .thenThrow(new InsufficientBalanceException("Failed"));
        assertThrows(InsufficientBalanceException.class, () -> userController.depositMoney(username, amountstr));
    }
    @Test
    public void testDepositMoneyException() throws Exception  {
        String username = "senderUsername";
        String amountstr = "100";
        int amount = Integer.parseInt(amountstr);

        when(balanceInformationService.IncreaseBalance(eq(username), eq(amount)))
                .thenThrow(new TransferFailedException("Failed"));
        assertThrows(TransferFailedException.class, () -> userController.depositMoney(username, amountstr));

        when(balanceInformationService.IncreaseBalance(eq(username), eq(amount)))
                .thenThrow(new UserNotFoundException("Failed"));
        assertThrows(UserNotFoundException.class, () -> userController.depositMoney(username, amountstr));

        when(balanceInformationService.IncreaseBalance(eq(username), eq(amount)))
                .thenThrow(new NumberFormatException());
        assertThrows(NumberFormatException.class, () -> userController.depositMoney(username, "amountstr"));



    }
    @Test
    public void testTransferMoneyException() throws Exception  {

        String username = "senderUsername";
        String amountstr = "100";
        String phone = "0913198018";
        int amount = Integer.parseInt(amountstr);

        when(balanceInformationService.TransferBalance(eq(username), eq(phone), eq(amount)))
                .thenThrow(new TransferFailedException("Failed"));
        assertThrows(TransferFailedException.class, () -> userController.transferMoney(username, amountstr, phone));

        when(balanceInformationService.TransferBalance(eq(username), eq(phone), eq(amount)))
                .thenThrow(new UserNotFoundException("Failed"));
        assertThrows(UserNotFoundException.class, () -> userController.transferMoney(username, amountstr, phone));

        when(balanceInformationService.TransferBalance(eq(username), eq("phone"), eq(amount)))
                .thenThrow(new InvalidPhoneNumberFormatException());
        assertThrows(InvalidPhoneNumberFormatException.class, () -> userController.transferMoney(username, amountstr, "phone"));

        when(balanceInformationService.TransferBalance(eq(username), eq(phone), eq(amount)))
                .thenThrow(new NumberFormatException());
        assertThrows(NumberFormatException.class, () -> userController.transferMoney(username, "amountstr", phone));

        when(balanceInformationService.TransferBalance(eq(username), eq(phone), eq(amount)))
                .thenThrow(new InsufficientBalanceException("Failed"));
        assertThrows(InsufficientBalanceException.class, () -> userController.transferMoney(username, amountstr, phone));

    }
}