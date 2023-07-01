package com.project.Controller;

import com.project.Assembler.BalanceResourceAssembler;
import com.project.Assembler.TransactionResourceAssembler;
import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Payload.DTO.TransactionHistoryDTO;
import com.project.Payload.DTO.UserDTO;
import com.project.Payload.Enum.TransactionType;
import com.project.Repository.BalanceInformationRepository;
import com.project.Repository.UserRepository;
import com.project.Service.BalanceInformationService;
import com.project.Service.TransactionHistoryService;
import com.project.Service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    BalanceInformationService balanceInformationService;
    @Autowired
    TransactionHistoryService transactionHistoryService;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    BalanceResourceAssembler    balanceResourceAssembler;
    @Autowired
    TransactionResourceAssembler transactionResourceAssembler;
    @GetMapping("/{username}/balance")
    public ResponseEntity<?> getOnlineBalance(@PathVariable("username") String username){

        BalanceInformationDTO balanceInformationDTO=  balanceInformationService.getUserBalanceInformationByUsername(username);
        return ResponseEntity.ok().body(balanceResourceAssembler.toModelWithWrapper(balanceInformationDTO,username));
    }
    @GetMapping("/{username}/deposit")
    public ResponseEntity<?> depositMoney(@PathVariable("username") String username,@RequestParam("amount") int amount){
        BalanceInformationDTO balanceInformationDTO=  balanceInformationService.getUserBalanceInformationByUsername(username);
        int current_amount = balanceInformationDTO.getBalance_amount();
        balanceInformationDTO.setBalance_amount(current_amount+ amount);
        TransactionHistoryDTO transactionHistoryDTO = new TransactionHistoryDTO(balanceInformationDTO.getUser(),
                                                    balanceInformationDTO.getUser(),
                 TransactionType.DEPOSIT.getDisplayName(),amount,new Date());
        balanceInformationService.saveBalanceInformation(balanceInformationDTO);
        transactionHistoryService.saveTransaction(transactionHistoryDTO);
        return ResponseEntity.ok().body(balanceResourceAssembler.toModelByTransaction(balanceInformationDTO,transactionHistoryDTO,username));
    }
    @GetMapping("/{username}/withdrawal")
    public ResponseEntity<?> withdrawalMoney(@PathVariable("username") String username,@RequestParam("amount") int amount){
        BalanceInformationDTO balanceInformationDTO=  balanceInformationService.getUserBalanceInformationByUsername(username);
        int current_amount = balanceInformationDTO.getBalance_amount();
        balanceInformationDTO.setBalance_amount(current_amount- amount);
        TransactionHistoryDTO transactionHistoryDTO = new TransactionHistoryDTO(balanceInformationDTO.getUser(),
                balanceInformationDTO.getUser(),
                TransactionType.WITHDRAWAL.getDisplayName(),amount,new Date());
        balanceInformationService.saveBalanceInformation(balanceInformationDTO);
        transactionHistoryService.saveTransaction(transactionHistoryDTO);
        return ResponseEntity.ok().body(balanceResourceAssembler.toModelByTransaction(balanceInformationDTO,transactionHistoryDTO,username));
    }
    @GetMapping("/{username}/transfer")
    public ResponseEntity<?> transferMoney(@PathVariable("username") String username,@RequestParam("amount") int amount,
                                           @RequestParam("phone") String phone){
        BalanceInformationDTO senderInformation=  balanceInformationService.getUserBalanceInformationByUsername(username);
        BalanceInformationDTO receiverInformation=  balanceInformationService.getUserBalanceInformationByPhone(phone);

        int current_sender_amount = senderInformation.getBalance_amount();
        int current_receiver_amount = receiverInformation.getBalance_amount();
        senderInformation.setBalance_amount(current_sender_amount- amount);
        receiverInformation.setBalance_amount(current_receiver_amount + amount);
        TransactionHistoryDTO transactionHistoryDTO = new TransactionHistoryDTO(senderInformation.getUser(),
                receiverInformation.getUser(),
                TransactionType.TRANSFER.getDisplayName(),amount,new Date());
        balanceInformationService.saveBalanceInformation(senderInformation);
        balanceInformationService.saveBalanceInformation(receiverInformation);
        transactionHistoryService.saveTransaction(transactionHistoryDTO);
        return ResponseEntity.ok().body(balanceResourceAssembler.toModelByTransferTransaction(senderInformation,receiverInformation,
                transactionHistoryDTO,username));
    }
    @GetMapping("/{username}/balance/history")
    public ResponseEntity<?> getHistory(@PathVariable("username") String username,
                                        @RequestParam(required = false,defaultValue = "0") Integer page,
                                        @RequestParam(required = false,defaultValue = "10")Integer size){
        Pageable pageable = PageRequest.of(page,size);
        Page<TransactionHistoryDTO> historyDTOS = transactionHistoryService.getAllTransactionHistory(pageable);
        return ResponseEntity.ok().body(transactionResourceAssembler.toCollectionModelWithUsername(historyDTOS,username));
    }
}
