package com.project.Controller;

import com.project.Assembler.BalanceResourceAssembler;
import com.project.Assembler.TransactionResourceAssembler;
import com.project.Exceptions.CustomExceptions.ValidationInput.InvalidPhoneNumberFormatException;
import com.project.Payload.DTO.BalanceInformationDTO;
import com.project.Payload.DTO.TransactionHistoryDTO;
import com.project.Payload.DTO.UserDTO;
import com.project.Payload.Enum.TransactionType;
import com.project.Payload.Request.BalanceCRUD;
import com.project.Payload.Response.ResponseEntityWrapper;
import com.project.Payload.Response.ResponsePagedEntityWrapper;
import com.project.Service.BalanceInformationService;
import com.project.Service.TransactionHistoryService;
import com.project.Service.UserDetailServiceImpl;
import com.project.Utils.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/user")
public class  UserController {
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
    @GetMapping("/balance")
    public ResponseEntity<?> getOnlineBalance(@RequestParam("username") String username){

        BalanceInformationDTO balanceInformationDTO=  balanceInformationService.getUserBalanceInformationByUsername(username);
        return ResponseEntity.ok().body(balanceResourceAssembler.toModelWithWrapper(balanceInformationDTO,username));
    }
    @PostMapping("/balance")
    public ResponseEntity<?> createBalance(@RequestBody BalanceCRUD balanceCRUD){
        UserDTO user = userDetailService.findByUsername(balanceCRUD.getUsername());
        BalanceInformationDTO balanceInformationDTO = new BalanceInformationDTO(UUID.randomUUID().toString(),user.getId_email(),balanceCRUD.getBalance_amount(),balanceCRUD.getPhone_number());
        balanceInformationService.saveBalanceInformation(balanceInformationDTO);
        ResponseEntityWrapper<?> responseEntityWrapper = new ResponseEntityWrapper<>("Successfully generated balance information");
        return ResponseEntity.ok().body(responseEntityWrapper);
    }

    @GetMapping("/deposit")
    public ResponseEntity<?> depositMoney(@RequestParam("username") String username,@RequestParam("amount") String amountstr){

        int amount = Integer.parseInt(amountstr);
        BalanceInformationDTO balanceInformationDTO=  balanceInformationService.IncreaseBalance(username,amount).get();
        int current_amount = balanceInformationDTO.getBalance_amount();
        balanceInformationDTO.setBalance_amount(current_amount+ amount);
        TransactionHistoryDTO transactionHistoryDTO = new TransactionHistoryDTO(UUID.randomUUID().toString(),balanceInformationDTO.getUser(),
                                                    balanceInformationDTO.getUser(),
                 TransactionType.DEPOSIT.getDisplayName(),amount,new Date());
        transactionHistoryService.saveTransaction(transactionHistoryDTO);
        ResponseEntityWrapper<TransactionHistoryDTO> responseEntityWrapper = balanceResourceAssembler.toModelByTransaction(balanceInformationDTO,transactionHistoryDTO,username);
        responseEntityWrapper.setMessage("Successfully deposit money");
        return ResponseEntity.ok().body(responseEntityWrapper);


    }
    @GetMapping("/withdrawal")
    public ResponseEntity<?> withdrawalMoney(@RequestParam("username") String username,@RequestParam("amount") String amountstr){
        int amount = Integer.parseInt(amountstr);
        BalanceInformationDTO balanceInformationDTO=  balanceInformationService.DecreaseBalance(username,amount).get();
        int current_amount = balanceInformationDTO.getBalance_amount();
        balanceInformationDTO.setBalance_amount(current_amount- amount);
        TransactionHistoryDTO transactionHistoryDTO = new TransactionHistoryDTO(UUID.randomUUID().toString(),balanceInformationDTO.getUser(),
                balanceInformationDTO.getUser(),
                TransactionType.WITHDRAWAL.getDisplayName(),amount,new Date());
        transactionHistoryService.saveTransaction(transactionHistoryDTO);
        ResponseEntityWrapper<TransactionHistoryDTO> responseEntityWrapper = balanceResourceAssembler.toModelByTransaction(balanceInformationDTO,transactionHistoryDTO,username);
        responseEntityWrapper.setMessage("Successfully withdrawal money");
        return ResponseEntity.ok().body(responseEntityWrapper);

    }

    @GetMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestParam("username") String username,@RequestParam("amount") String amountstr,
                                           @RequestParam("phone") String phone){

        int amount = Integer.parseInt(amountstr);
        if(!PhoneNumberValidator.isValidPhoneNumber(phone)){
            throw  new InvalidPhoneNumberFormatException();
        }
        List<Optional<BalanceInformationDTO>> optionalList = balanceInformationService.TransferBalance(username,phone,amount);
        BalanceInformationDTO senderInformation =  optionalList.get(0).get();
        BalanceInformationDTO receiverInformation= optionalList.get(1).get();

        TransactionHistoryDTO transactionHistoryDTO = new TransactionHistoryDTO(UUID.randomUUID().toString(),senderInformation.getUser(),
                receiverInformation.getUser(),
                TransactionType.TRANSFER.getDisplayName(),amount,new Date());
        transactionHistoryService.saveTransaction(transactionHistoryDTO);
        ResponseEntityWrapper<TransactionHistoryDTO> responseEntityWrapper =balanceResourceAssembler.toModelByTransferTransaction(senderInformation,receiverInformation,
                transactionHistoryDTO,username);
        responseEntityWrapper.setMessage("Successfully transfer money");

        return ResponseEntity.ok().body(responseEntityWrapper);
    }
    @GetMapping("/balance/history")
    public ResponseEntity<?> getHistory(@RequestParam("username") String username,
                                        @RequestParam(required = false,defaultValue = "0") String  pagestr,
                                        @RequestParam(required = false,defaultValue = "10")String sizestr){
        int page = Integer.parseInt(pagestr);
        int size = Integer.parseInt(sizestr);
        Pageable pageable = PageRequest.of(page,size);
        Page<TransactionHistoryDTO> historyDTOS = transactionHistoryService.getAllTransactionHistory(pageable);
        ResponsePagedEntityWrapper<?> responsePagedEntityWrapper = transactionResourceAssembler.toCollectionModelWithUsername(historyDTOS,username);
        responsePagedEntityWrapper.setMessage("successfully fetch user history");
        return ResponseEntity.ok().body(responsePagedEntityWrapper);
    }
}
