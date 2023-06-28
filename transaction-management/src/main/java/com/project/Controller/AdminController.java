package com.project.Controller;

import com.project.Assembler.BalanceResourceAssembler;
import com.project.Assembler.TransactionResourceAssembler;
import com.project.Payload.DTO.TransactionHistoryDTO;
import com.project.Service.BalanceInformationService;
import com.project.Service.TransactionHistoryService;
import com.project.Service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    BalanceInformationService balanceInformationService;
    @Autowired
    TransactionHistoryService transactionHistoryService;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    BalanceResourceAssembler balanceResourceAssembler;
    @Autowired
    TransactionResourceAssembler transactionResourceAssembler;
    @GetMapping("/history")
    public ResponseEntity<?> getHistoryofUser(@RequestParam(value="username") String username,
                                              @RequestParam(value="startDate",required = false)String startDate,
                                              @RequestParam(value="startDate",required = false)String endDate,
                                              @RequestParam(value = "type", required = false) String type){
        List<TransactionHistoryDTO> historyDTOS = transactionHistoryService.getAllTransactionHistory();
        return ResponseEntity.ok().body(transactionResourceAssembler.toCollectionModelWithUsername(historyDTOS,username));
    }
}
