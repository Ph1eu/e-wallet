package com.project.controller;

import com.project.assembler.BalanceResourceAssembler;
import com.project.assembler.TransactionResourceAssembler;
import com.project.exceptions.custom_exceptions.ValidationInput.InvalidDateFormatException;
import com.project.payload.dto.TransactionHistoryDTO;
import com.project.service.BalanceInformationService;
import com.project.service.TransactionHistoryService;
import com.project.service.UserDetailServiceImpl;
import com.project.Utils.StringDateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
                                              @RequestParam(value="startDate",required = false)String startDateString,
                                              @RequestParam(value="endDate",required = false)String endDateString,
                                              @RequestParam(value = "type", required = false) String type,
                                              @RequestParam(required = false,defaultValue = "0") String pagestr,
                                              @RequestParam(required = false,defaultValue = "7")String sizestr) throws ParseException {

        int page = Integer.parseInt(pagestr);
        int size = Integer.parseInt(sizestr);
        Page<TransactionHistoryDTO> historyDTOS = null;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        Date startDate = null;
        Date endDate = null;
            // Parse start date if provided
        if (startDateString != null) {
                if(!StringDateValidator.isValidDateFormat(startDateString)){
                    throw new InvalidDateFormatException(startDateString,"dd-MM-yyyy");
                }
                startDate = dateFormat.parse(startDateString);
        }

            // Parse end date if provided
        if (endDateString != null) {
            if(!StringDateValidator.isValidDateFormat(startDateString)){
                throw new InvalidDateFormatException(startDateString,"dd-MM-yyyy");
            }
            endDate = dateFormat.parse(endDateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            endDate = calendar.getTime();
        }

        PageRequest pageable = PageRequest.of(page, size);
        // all params are null
        if(startDate == null && endDate == null && type == null ){
             historyDTOS = transactionHistoryService.getAllTransactionHistory(pageable);
            return ResponseEntity.ok().body(transactionResourceAssembler.toCollectionModelWithUsername(historyDTOS,username));
        // no given range
        } else if (startDate == null && endDate == null ) {
            historyDTOS = transactionHistoryService.getAllTransactionHistoryByType(type,pageable);
            return ResponseEntity.ok().body(transactionResourceAssembler.toCollectionModelWithUsername(historyDTOS,username));
        // no given type
        } else if (type == null && endDate != null && startDate != null) {
            historyDTOS = transactionHistoryService.getAllTransactionHistoryByRange(startDate,endDate,pageable);
            return ResponseEntity.ok().body(transactionResourceAssembler.toCollectionModelWithUsername(historyDTOS,username));
        // no given type and end date
        } else if (type == null && endDate == null) {
            historyDTOS = transactionHistoryService.getAllTransactionHistoryByStartDate(startDate,pageable);
            return ResponseEntity.ok().body(transactionResourceAssembler.toCollectionModelWithUsername(historyDTOS,username));
        // no given start date and type
        } else if (startDate == null && type == null) {
            historyDTOS = transactionHistoryService.getAllTransactionHistoryByEndDate(endDate,pageable);
            return ResponseEntity.ok().body(transactionResourceAssembler.toCollectionModelWithUsername(historyDTOS,username));
        }else {
            historyDTOS = transactionHistoryService.getAllTransactionHistoryByFilter(startDate,endDate,type,pageable);
            return ResponseEntity.ok().body(transactionResourceAssembler.toCollectionModelWithUsername(historyDTOS,username));
        }

    }
}
