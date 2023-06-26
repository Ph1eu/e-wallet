package com.project.Unit.Service;

import com.project.Model.TransactionHistory;
import com.project.Model.User;
import com.project.Payload.DTO.TransactionHistoryDTO;
import com.project.Repository.BalanceInformationRepository;
import com.project.Repository.TransactionHistoryRepository;
import com.project.Repository.UserRepository;
import com.project.Service.TransactionHistoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionHistoryServiceTest {
    @Mock
    BalanceInformationRepository balanaceInformationRepository;
    @Mock
    TransactionHistoryRepository transactionHistoryRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    TransactionHistoryService transactionHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void saveTransaction() {
        TransactionHistoryDTO transactionHistoryDTO = new TransactionHistoryDTO();
        transactionHistoryDTO.setSenderid("senderId");
        transactionHistoryDTO.setRecipientid("recipientId");

        User mockSender = new User();
        mockSender.setIdemail("senderId");


        User mockRecipient = new User();
        mockRecipient.setId_email("recipientId");
        Mockito.when(userRepository.getReferenceById(transactionHistoryDTO.getSenderid()))
                .thenReturn(mockSender);
        Mockito.when(userRepository.getReferenceById(transactionHistoryDTO.getRecipientid()))
                .thenReturn(mockRecipient);

        // Call the method under test
        Assertions.assertDoesNotThrow(() -> transactionHistoryService.saveTransaction(transactionHistoryDTO));

        // Verify the expected interactions
        Mockito.verify(userRepository).getReferenceById(transactionHistoryDTO.getSenderid());
        Mockito.verify(userRepository).getReferenceById(transactionHistoryDTO.getRecipientid());
        Mockito.verify(transactionHistoryRepository).save(Mockito.any(TransactionHistory.class));
    }

    @Test
    void getAllTransactionHistory() {

        String senderId = "senderId";
        String recipientId = "recipientId";

        User sender = new User();
        sender.setIdemail(senderId);

        User recipient = new User();
        recipient.setIdemail(recipientId);
        List<TransactionHistory> mockTransactionHistoryList = new ArrayList<>();
        TransactionHistory mockTransactionHistory1 = new TransactionHistory();
        mockTransactionHistory1.setId("transactionId1");
        mockTransactionHistory1.setSender(sender);
        mockTransactionHistory1.setRecipient(recipient);
        mockTransactionHistory1.setTransaction_type("transactionType");
        mockTransactionHistory1.setAmount(1000);
        mockTransactionHistory1.setTransaction_date(new Date());
        TransactionHistory mockTransactionHistory2 = new TransactionHistory();
        mockTransactionHistory2.setId("transactionId2");
        mockTransactionHistory2.setSender(sender);
        mockTransactionHistory2.setRecipient(recipient);
        mockTransactionHistory2.setTransaction_type("transactionType");
        mockTransactionHistory2.setAmount(1000);
        mockTransactionHistory2.setTransaction_date(new Date());
        mockTransactionHistoryList.add(mockTransactionHistory1);
        mockTransactionHistoryList.add(mockTransactionHistory2);

        Mockito.when(transactionHistoryRepository.findAll())
                .thenReturn(mockTransactionHistoryList);

        // Call the method under test
        List<TransactionHistoryDTO> result = transactionHistoryService.getAllTransactionHistory();

        // Verify the expected interaction
        Mockito.verify(transactionHistoryRepository).findAll();

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockTransactionHistoryList.size(), result.size());
    }

    @Test
    void getAllTransactionHistoryByAmount() {
        int amount = 1000;

        String senderId = "senderId";
        String recipientId = "recipientId";

        User sender = new User();
        sender.setIdemail(senderId);

        User recipient = new User();
        recipient.setIdemail(recipientId);
        List<TransactionHistory> mockTransactionHistoryList = new ArrayList<>();
        TransactionHistory mockTransactionHistory1 = new TransactionHistory();
        mockTransactionHistory1.setId("transactionId1");
        mockTransactionHistory1.setSender(sender);
        mockTransactionHistory1.setRecipient(recipient);
        mockTransactionHistory1.setTransaction_type("transactionType");
        mockTransactionHistory1.setAmount(1000);
        mockTransactionHistory1.setTransaction_date(new Date());
        TransactionHistory mockTransactionHistory2 = new TransactionHistory();
        mockTransactionHistory2.setId("transactionId2");
        mockTransactionHistory2.setSender(sender);
        mockTransactionHistory2.setRecipient(recipient);
        mockTransactionHistory2.setTransaction_type("transactionType");
        mockTransactionHistory2.setAmount(1000);
        mockTransactionHistory2.setTransaction_date(new Date());
        mockTransactionHistoryList.add(mockTransactionHistory1);
        mockTransactionHistoryList.add(mockTransactionHistory2);
        Mockito.when(transactionHistoryRepository.findTransactionHistoryByAmount(amount))
                .thenReturn(mockTransactionHistoryList);
        List<TransactionHistoryDTO> result = transactionHistoryService.getAllTransactionHistoryByAmount(amount);
        // Verify the expected interaction
        Mockito.verify(transactionHistoryRepository).findTransactionHistoryByAmount(amount);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockTransactionHistoryList.size(), result.size());

    }

    @Test
    void getAllTransactionHistoryBySender() {
        String senderId = "senderId";
        String recipientId = "recipientId";

        User sender = new User();
        sender.setIdemail(senderId);

        User recipient = new User();
        recipient.setIdemail(recipientId);
        List<TransactionHistory> mockTransactionHistoryList = new ArrayList<>();
        TransactionHistory mockTransactionHistory1 = new TransactionHistory();
        mockTransactionHistory1.setId("transactionId1");
        mockTransactionHistory1.setSender(sender);
        mockTransactionHistory1.setRecipient(recipient);
        mockTransactionHistory1.setTransaction_type("transactionType");
        mockTransactionHistory1.setAmount(1000);
        mockTransactionHistory1.setTransaction_date(new Date());
        TransactionHistory mockTransactionHistory2 = new TransactionHistory();
        mockTransactionHistory2.setId("transactionId2");
        mockTransactionHistory2.setSender(sender);
        mockTransactionHistory2.setRecipient(recipient);
        mockTransactionHistory2.setTransaction_type("transactionType");
        mockTransactionHistory2.setAmount(1000);
        mockTransactionHistory2.setTransaction_date(new Date());
        mockTransactionHistoryList.add(mockTransactionHistory1);
        mockTransactionHistoryList.add(mockTransactionHistory2);
        mockTransactionHistoryList.add(mockTransactionHistory2);Mockito.when(transactionHistoryRepository.findTransactionHistoryBySender(senderId))
                .thenReturn(mockTransactionHistoryList);

        // Call the method under test
        List<TransactionHistoryDTO> result = transactionHistoryService.getAllTransactionHistoryBySender(senderId);

        // Verify the expected interaction
        Mockito.verify(transactionHistoryRepository).findTransactionHistoryBySender(senderId);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockTransactionHistoryList.size(), result.size());
    }

    @Test
    void getAllTransactionHistoryByRecipient() {
        String senderId = "senderId";
        String recipientId = "recipientId";

        User sender = new User();
        sender.setIdemail(senderId);

        User recipient = new User();
        recipient.setIdemail(recipientId);
        List<TransactionHistory> mockTransactionHistoryList = new ArrayList<>();
        TransactionHistory mockTransactionHistory1 = new TransactionHistory();
        mockTransactionHistory1.setId("transactionId1");
        mockTransactionHistory1.setSender(sender);
        mockTransactionHistory1.setRecipient(recipient);
        mockTransactionHistory1.setTransaction_type("transactionType");
        mockTransactionHistory1.setAmount(1000);
        mockTransactionHistory1.setTransaction_date(new Date());
        TransactionHistory mockTransactionHistory2 = new TransactionHistory();
        mockTransactionHistory2.setId("transactionId2");
        mockTransactionHistory2.setSender(sender);
        mockTransactionHistory2.setRecipient(recipient);
        mockTransactionHistory2.setTransaction_type("transactionType");
        mockTransactionHistory2.setAmount(1000);
        mockTransactionHistory2.setTransaction_date(new Date());
        mockTransactionHistoryList.add(mockTransactionHistory1);
        mockTransactionHistoryList.add(mockTransactionHistory2);
        Mockito.when(transactionHistoryRepository.findTransactionHistoryBySender(recipientId))
                .thenReturn(mockTransactionHistoryList);

        // Call the method under test
        List<TransactionHistoryDTO> result = transactionHistoryService.getAllTransactionHistoryByRecipient(recipientId);

        // Verify the expected interaction
        Mockito.verify(transactionHistoryRepository).findTransactionHistoryBySender(recipientId);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockTransactionHistoryList.size(), result.size());
    }
}