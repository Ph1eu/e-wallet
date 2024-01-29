package com.project.unit.Controller;

import com.project.assembler.BalanceResourceAssembler;
import com.project.controller.UserController;
import com.project.exceptions.custom_exceptions.BusinessLogic.InsufficientBalanceException;
import com.project.exceptions.custom_exceptions.BusinessLogic.TransferFailedException;
import com.project.exceptions.custom_exceptions.BusinessLogic.UserNotFoundException;
import com.project.exceptions.custom_exceptions.ValidationInput.InvalidPhoneNumberFormatException;
import com.project.service.BalanceInformationService;
import com.project.service.TransactionHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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