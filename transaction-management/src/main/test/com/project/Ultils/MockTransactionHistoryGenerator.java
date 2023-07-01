package com.project.Ultils;
import com.project.Model.TransactionHistory;
import com.project.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class MockTransactionHistoryGenerator {

    private static final int MAX_AMOUNT = 1000;
    public  List<TransactionHistory> generateMockTransactions(int count) {
        List<TransactionHistory> transactions = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            TransactionHistory transaction = generateMockTransaction();
            transactions.add(transaction);
        }

        return transactions;
    }
    public  TransactionHistory generateMockTransaction() {
        TransactionHistory transaction = new TransactionHistory();
        MockUserGenerator mockUserGenerator = new MockUserGenerator();
        // Generate random UUID for the transaction ID
        transaction.setId(UUID.randomUUID().toString());

        // Generate random sender and recipient
        User sender = new User(mockUserGenerator.generateMockUser(1));
        User recipient = new User(mockUserGenerator.generateMockUser(2));

        transaction.setSender(sender);
        transaction.setRecipient(recipient);

        // Generate random transaction type
        String[] transactionTypes = {"Deposit", "Transfer", "Withdrawal"};
        Random random = new Random();
        int typeIndex = random.nextInt(transactionTypes.length);
        transaction.setTransaction_type(transactionTypes[typeIndex]);

        // Generate random amount
        int amount = random.nextInt(MAX_AMOUNT) + 1;
        transaction.setAmount(amount);

        // Set current date as transaction date
        transaction.setTransaction_date(new Date());

        return transaction;
    }


    public List<TransactionHistory> generateTransactionWithinDateRange(int count, Date startDate, Date endDate) {
        List<TransactionHistory> transactions = new ArrayList<>();
        Date currentDate = startDate;
        for (int i = 0; i < count; i++) {
            if (currentDate.after(endDate)) {
                break; // Stop generating transactions if the current date is after the specified end date
            }
            TransactionHistory transaction = generateMockTransaction();
            transaction.setTransaction_date(currentDate);
            transactions.add(transaction);
            currentDate = addDays(currentDate, 1); // Increment the date by 1 day
        }
        return transactions;
    }

    public List<TransactionHistory> generateTransactionWithStartDate(int count, Date startDate) {
        List<TransactionHistory> transactions = new ArrayList<>();
        Date currentDate = startDate;
        for (int i = 0; i < count; i++) {
            TransactionHistory transaction = generateMockTransaction();

            transaction.setTransaction_date(currentDate);
            transactions.add(transaction);
            currentDate = addDays(currentDate, 1); // Increment the date by 1 day
        }
        return transactions;
    }
    public List<TransactionHistory> generateTransactionWithEndDate(int count, Date endDate) {
        List<TransactionHistory> transactions = new ArrayList<>();
        Date currentDate = endDate;
        for (int i = 0; i < count; i++) {
            TransactionHistory transaction = generateMockTransaction();            // Generate mock transaction details

            transaction.setTransaction_date(currentDate);
            transactions.add(transaction);
            currentDate = subtractDays(currentDate, 1); // Decrement the date by 1 day

        }
        return transactions;
    }
    private Date subtractDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        return calendar.getTime();
    }
    private Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }



}
