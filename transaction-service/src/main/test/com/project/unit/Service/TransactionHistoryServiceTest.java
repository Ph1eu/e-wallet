package com.project.unit.Service;

import com.project.model.TransactionHistory;
import com.project.model.User;
import com.project.payload.dto.TransactionHistoryDTO;
import com.project.repository.BalanceInformationRepository;
import com.project.repository.TransactionHistoryRepository;
import com.project.repository.UserRepository;
import com.project.service.TransactionHistoryService;
import com.project.ultils.DateUtils;
import com.project.ultils.MockTransactionHistoryGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        DateUtils DateUtils = new DateUtils();
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
        MockTransactionHistoryGenerator mockTransactionHistoryGenerator = new MockTransactionHistoryGenerator();

        List<TransactionHistory> mockTransactionHistoryList = mockTransactionHistoryGenerator.generateMockTransactions(2);

        Pageable pageable = PageRequest.of(0, 2);
        Page<TransactionHistory> mockPage = new PageImpl<>(mockTransactionHistoryList);

        Mockito.when(transactionHistoryRepository.findAll(pageable))
                .thenReturn(mockPage);
        // Call the method under test
        Page<TransactionHistoryDTO> result = transactionHistoryService.getAllTransactionHistory(pageable);

        // Verify the expected interaction
        Mockito.verify(transactionHistoryRepository).findAll(pageable);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockTransactionHistoryList.size(), result.getContent().size());
    }

    @Test
    void getAllTransactionHistoryByFilter() throws ParseException {
        int object_count = 50;
        int size = 10;
        //int num_page = (int) Math.ceil((double) object_count / size);
        Date start_date = DateUtils.convertStringToDate("10/01/2002", "dd/MM/yyyy");
        Date end_date = DateUtils.convertStringToDate("20/05/2002", "dd/MM/yyyy");
        MockTransactionHistoryGenerator mockTransactionHistoryGenerator = new MockTransactionHistoryGenerator();

        List<TransactionHistory> mockTransactionHistoryList = mockTransactionHistoryGenerator.generateTransactionWithinDateRange(object_count, start_date, end_date);
        List<List<TransactionHistory>> paginatedList = new ArrayList<>();
        List<TransactionHistory> mockDepositTransactionHistoryList = new ArrayList<>();
        for (TransactionHistory transactionHistory : mockTransactionHistoryList) {
            if (transactionHistory.getTransaction_type().equals("Deposit")) {
                mockDepositTransactionHistoryList.add(transactionHistory);
            }
        }
        int num_page = (int) Math.ceil((double) mockDepositTransactionHistoryList.size() / size);
        for (int pageIndex = 0; pageIndex < num_page; pageIndex++) {
            int fromIndex = pageIndex * size;
            int toIndex = Math.min(fromIndex + size, mockDepositTransactionHistoryList.size());

            List<TransactionHistory> page = mockDepositTransactionHistoryList.subList(fromIndex, toIndex);
            paginatedList.add(page);
        }
        for (int pageIndex = 0; pageIndex < paginatedList.size(); pageIndex++) {
            List<TransactionHistory> page = paginatedList.get(pageIndex);

            Page<TransactionHistory> mockPage = new PageImpl<>(page);
            Pageable pageable = PageRequest.of(pageIndex, size);
            Mockito.when(transactionHistoryRepository.findTransactionHistoryByRange(start_date, end_date, pageable))
                    .thenReturn(mockPage);
            Page<TransactionHistoryDTO> result = transactionHistoryService.getAllTransactionHistoryByRange(start_date, end_date, pageable);
            Mockito.verify(transactionHistoryRepository).findTransactionHistoryByRange(start_date, end_date, pageable);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(page.size(), result.getContent().size());
            for (int i = 0; i < result.getContent().size(); i++) {
                TransactionHistoryDTO transactionHistory = result.getContent().get(i);
                if (pageIndex == 0 && i == 0) {
                    Assertions.assertTrue((transactionHistory.getTransaction_date().after(start_date) || transactionHistory.getTransaction_date().equals(start_date)) &&
                            transactionHistory.getTransaction_date().before(end_date));
                } else if (pageIndex == num_page && i == (result.getContent().size() - 1)) {
                    Assertions.assertTrue(transactionHistory.getTransaction_date().after(start_date) &&
                            (transactionHistory.getTransaction_date().before(end_date) || transactionHistory.getTransaction_date().equals(end_date)));
                } else {
                    Assertions.assertTrue(transactionHistory.getTransaction_date().after(start_date) &&
                            transactionHistory.getTransaction_date().before(end_date));
                }
                assertEquals("Deposit", transactionHistory.getTransaction_type());

            }

        }

    }

    @Test
    void getAllTransactionHistoryByRange() throws ParseException {
        int object_count = 50;
        int size = 10;
        int num_page = (int) Math.ceil((double) object_count / size);
        Date start_date = DateUtils.convertStringToDate("10/01/2002", "dd/MM/yyyy");
        Date end_date = DateUtils.convertStringToDate("20/05/2002", "dd/MM/yyyy");
        MockTransactionHistoryGenerator mockTransactionHistoryGenerator = new MockTransactionHistoryGenerator();

        List<TransactionHistory> mockTransactionHistoryList = mockTransactionHistoryGenerator.generateTransactionWithinDateRange(object_count, start_date, end_date);
        List<List<TransactionHistory>> paginatedList = new ArrayList<>();

        for (int pageIndex = 0; pageIndex < num_page; pageIndex++) {
            int fromIndex = pageIndex * size;
            int toIndex = Math.min(fromIndex + size, object_count);

            List<TransactionHistory> page = mockTransactionHistoryList.subList(fromIndex, toIndex);
            paginatedList.add(page);
        }
        for (int pageIndex = 0; pageIndex < paginatedList.size(); pageIndex++) {
            List<TransactionHistory> page = paginatedList.get(pageIndex);

            Page<TransactionHistory> mockPage = new PageImpl<>(page);
            Pageable pageable = PageRequest.of(pageIndex, size);
            Mockito.when(transactionHistoryRepository.findTransactionHistoryByRange(start_date, end_date, pageable))
                    .thenReturn(mockPage);
            Page<TransactionHistoryDTO> result = transactionHistoryService.getAllTransactionHistoryByRange(start_date, end_date, pageable);
            Mockito.verify(transactionHistoryRepository).findTransactionHistoryByRange(start_date, end_date, pageable);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(page.size(), result.getContent().size());
            for (int i = 0; i < result.getContent().size(); i++) {
                TransactionHistoryDTO transactionHistory = result.getContent().get(i);
                if (pageIndex == 0 && i == 0) {
                    Assertions.assertTrue(transactionHistory.getTransaction_date().equals(start_date) &&
                            transactionHistory.getTransaction_date().before(end_date));
                } else if (pageIndex == num_page && i == (result.getContent().size() - 1)) {
                    Assertions.assertTrue(transactionHistory.getTransaction_date().after(start_date) &&
                            transactionHistory.getTransaction_date().equals(end_date));
                } else {
                    Assertions.assertTrue(transactionHistory.getTransaction_date().after(start_date) &&
                            transactionHistory.getTransaction_date().before(end_date));
                }

            }

        }
    }

    @Test
    void getAllTransactionHistoryByStartDate() throws ParseException {
        int object_count = 50;
        int size = 10;
        int num_page = (int) Math.ceil((double) object_count / size);
        Date start_date = DateUtils.convertStringToDate("10/01/2002", "dd/MM/yyyy");
        MockTransactionHistoryGenerator mockTransactionHistoryGenerator = new MockTransactionHistoryGenerator();

        List<TransactionHistory> mockTransactionHistoryList = mockTransactionHistoryGenerator.generateTransactionWithStartDate(object_count, start_date);
        List<List<TransactionHistory>> paginatedList = new ArrayList<>();

        for (int pageIndex = 0; pageIndex < num_page; pageIndex++) {
            int fromIndex = pageIndex * size;
            int toIndex = Math.min(fromIndex + size, object_count);

            List<TransactionHistory> page = mockTransactionHistoryList.subList(fromIndex, toIndex);
            paginatedList.add(page);
        }
        for (int pageIndex = 0; pageIndex < paginatedList.size(); pageIndex++) {
            List<TransactionHistory> page = paginatedList.get(pageIndex);

            Page<TransactionHistory> mockPage = new PageImpl<>(page);
            Pageable pageable = PageRequest.of(pageIndex, size);
            Mockito.when(transactionHistoryRepository.findTransactionHistoryByStartDate(start_date, pageable))
                    .thenReturn(mockPage);
            Page<TransactionHistoryDTO> result = transactionHistoryService.getAllTransactionHistoryByStartDate(start_date, pageable);
            Mockito.verify(transactionHistoryRepository).findTransactionHistoryByStartDate(start_date, pageable);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(page.size(), result.getContent().size());
            for (int i = 0; i < result.getContent().size(); i++) {
                TransactionHistoryDTO transactionHistory = result.getContent().get(i);
                if (pageIndex == 0 && i == 0) {
                    assertEquals(transactionHistory.getTransaction_date(), start_date);

                } else {
                    Assertions.assertTrue(transactionHistory.getTransaction_date().after(start_date));

                }
            }

        }
    }

    @Test
    void getAllTransactionHistoryByEndDate() throws ParseException {
        int object_count = 50;
        int size = 10;
        int num_page = (int) Math.ceil((double) object_count / size);
        Date end_date = DateUtils.convertStringToDate("20/05/2002", "dd/MM/yyyy");
        MockTransactionHistoryGenerator mockTransactionHistoryGenerator = new MockTransactionHistoryGenerator();

        List<TransactionHistory> mockTransactionHistoryList = mockTransactionHistoryGenerator.generateTransactionWithEndDate(object_count, end_date);
        List<List<TransactionHistory>> paginatedList = new ArrayList<>();

        for (int pageIndex = 0; pageIndex < num_page; pageIndex++) {
            int fromIndex = pageIndex * size;
            int toIndex = Math.min(fromIndex + size, object_count);

            List<TransactionHistory> page = mockTransactionHistoryList.subList(fromIndex, toIndex);
            paginatedList.add(page);
        }
        for (int pageIndex = 0; pageIndex < paginatedList.size(); pageIndex++) {
            List<TransactionHistory> page = paginatedList.get(pageIndex);

            Page<TransactionHistory> mockPage = new PageImpl<>(page);
            Pageable pageable = PageRequest.of(pageIndex, size);
            Mockito.when(transactionHistoryRepository.findTransactionHistoryByEndDate(end_date, pageable))
                    .thenReturn(mockPage);
            Page<TransactionHistoryDTO> result = transactionHistoryService.getAllTransactionHistoryByEndDate(end_date, pageable);
            Mockito.verify(transactionHistoryRepository).findTransactionHistoryByEndDate(end_date, pageable);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(page.size(), result.getContent().size());
            for (int i = 0; i < result.getContent().size(); i++) {
                TransactionHistoryDTO transactionHistory = result.getContent().get(i);
                if (pageIndex == 0 && i == 0) {
                    System.out.println(transactionHistory.getTransaction_date());
                    Assertions.assertTrue(transactionHistory.getTransaction_date().equals(end_date));
                } else {
                    Assertions.assertTrue(transactionHistory.getTransaction_date().before(end_date));
                }
            }

        }
    }

    @Test
    void getAllTransactionHistoryByType() {
        int object_count = 50;
        int size = 5;
        MockTransactionHistoryGenerator mockTransactionHistoryGenerator = new MockTransactionHistoryGenerator();

        List<TransactionHistory> mockTransactionHistoryList = mockTransactionHistoryGenerator.generateMockTransactions(object_count);
        List<List<TransactionHistory>> paginatedList = new ArrayList<>();
        List<TransactionHistory> mockDepositTransactionHistoryList = new ArrayList<>();
        for (TransactionHistory transactionHistory : mockTransactionHistoryList) {
            if (transactionHistory.getTransaction_type().equals("Deposit")) {
                mockDepositTransactionHistoryList.add(transactionHistory);
            }
        }
        int num_page = (int) Math.ceil((double) mockDepositTransactionHistoryList.size() / size);
        for (int pageIndex = 0; pageIndex < num_page; pageIndex++) {
            int fromIndex = pageIndex * size;
            int toIndex = Math.min(fromIndex + size, mockDepositTransactionHistoryList.size());

            List<TransactionHistory> page = mockDepositTransactionHistoryList.subList(fromIndex, toIndex);
            paginatedList.add(page);
        }
        for (int pageIndex = 0; pageIndex < paginatedList.size(); pageIndex++) {
            List<TransactionHistory> page = paginatedList.get(pageIndex);

            Page<TransactionHistory> mockPage = new PageImpl<>(page);
            Pageable pageable = PageRequest.of(pageIndex, size);
            Mockito.when(transactionHistoryRepository.findTransactionHistoryByTransactionType("Deposit", pageable))
                    .thenReturn(mockPage);
            Page<TransactionHistoryDTO> result = transactionHistoryService.getAllTransactionHistoryByType("Deposit", pageable);
            Mockito.verify(transactionHistoryRepository).findTransactionHistoryByTransactionType("Deposit", pageable);
            Assertions.assertNotNull(result);
            Assertions.assertEquals(page.size(), result.getContent().size());
            for (int i = 0; i < result.getContent().size(); i++) {
                TransactionHistoryDTO transactionHistory = result.getContent().get(i);
                assertEquals("Deposit", transactionHistory.getTransaction_type());
            }

        }
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
        mockTransactionHistoryList.add(mockTransactionHistory2);
        Mockito.when(transactionHistoryRepository.findTransactionHistoryBySender(senderId))
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