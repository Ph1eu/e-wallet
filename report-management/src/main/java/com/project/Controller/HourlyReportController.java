package com.project.Controller;

import com.project.Assembler.HourlyReportAssembler;
import com.project.Payload.DTO.HourlyReportDTO;
import com.project.Payload.Response.ResponseEntityWrapper;
import com.project.Payload.Response.ResponsePagedEntityWrapper;
import com.project.Payload.Response.WindowResult;
import com.project.Service.HourlyReportService;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/report")
public class HourlyReportController {
    @Autowired
    HourlyReportService hourlyReportService;

    @Autowired
    HourlyReportAssembler hourlyReportAssembler;

    @GetMapping("/hourlyreport")
    public ResponseEntity<?> getHourlyReport(@RequestParam("startDate") String startDateString ,
                                             @RequestParam(value = "endDate",required = false) String endDateString,
                                             @RequestParam(value = "startTime", required = false) Integer startTime,
                                             @RequestParam(value = "page",defaultValue = "0") Integer page,
                                             @RequestParam(value = "size",defaultValue = "7") Integer size
                                             ){

        Page<HourlyReportDTO> hourlyReportDTOS = null;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        Date startDate = null;
        Date endDate = null;


        System.out.println();
        try {
            // Parse start date if provided
            if (startDateString != null) {
                startDate = dateFormat.parse(startDateString);
            }

            // Parse end date if provided
            if (endDateString != null) {
                endDate = dateFormat.parse(endDateString);

            }
        } catch (ParseException e) {
            // Handle the exception if the input date strings are not in the expected format
            return ResponseEntity.badRequest().body(new ResponseEntityWrapper<>("Bad date request"));
        }

        PageRequest pageable = PageRequest.of(page, size);
        ResponsePagedEntityWrapper<EntityModel<HourlyReportDTO>> responsePagedEntityWrapper= null;
        if(startDate != null && endDate != null && startTime == null )
        {
            hourlyReportDTOS = hourlyReportService.generateHourlyReports(startDate,endDate,pageable);
            responsePagedEntityWrapper= hourlyReportAssembler.toCollectionModelwithWrapper(hourlyReportDTOS);
            responsePagedEntityWrapper.setMessage("Generated report successfully");
        } else if (startDate != null && endDate == null && startTime == null ) {
            hourlyReportDTOS = hourlyReportService.generateHourlyReportsByDate(startDate,pageable);
            responsePagedEntityWrapper= hourlyReportAssembler.toCollectionModelwithWrapper(hourlyReportDTOS);
            responsePagedEntityWrapper.setMessage("Generated report successfully");
        } else if (startDate != null && endDate == null) {
            hourlyReportDTOS = hourlyReportService.generateHourlyReportsByDateTime(startDate,startTime,pageable);
            responsePagedEntityWrapper= hourlyReportAssembler.toCollectionModelwithWrapper(hourlyReportDTOS);
            responsePagedEntityWrapper.setMessage("Generated report successfully");
        }else{
            responsePagedEntityWrapper = new ResponsePagedEntityWrapper<>();
            responsePagedEntityWrapper.setMessage("fail to generate report due to wrong parameter choices");
        }

        return ResponseEntity.ok().body(responsePagedEntityWrapper);

    }
//    @GetMapping("/kafka")
//    public ResponseEntity<?>getall(){
//     hourlyReportService.StreamTransactions();
//        return ResponseEntity.ok().body("sucess");
//    }
    @GetMapping("/publish")
    public ResponseEntity<?>publish(){
        hourlyReportService.PublishTransactions();
        return ResponseEntity.ok().body("sucesss");
    }
    @GetMapping("/onesec")
    public ResponseEntity<?>oneSecReport(){
        try {
            Optional<GenericRecord> result = hourlyReportService.ReceiveLatestRecord();
            if (Double.parseDouble(result.get().get("total_transaction_amount").toString())==0){
                GenericRecord genericRecord = result.get();


                ResponseEntityWrapper<WindowResult> entityWrapper = new ResponseEntityWrapper<>("There is no transaction in the system");
                WindowResult windowResult = new WindowResult();
                windowResult.setTotal_amount(Double.parseDouble(genericRecord.get("total_transaction_amount").toString()));
                windowResult.setTotal_count(Integer.parseInt(genericRecord.get("total_record_count").toString()));
                windowResult.setStartTimeWithString(genericRecord.get("start_time").toString());
                windowResult.setEndTimeWithString(genericRecord.get("end_time").toString());
                entityWrapper.setData(List.of(windowResult));
                return    ResponseEntity.ok().body(entityWrapper);
            }
            else{
                GenericRecord genericRecord = result.get();
                WindowResult windowResult = new WindowResult();
                windowResult.setTotal_amount(Double.parseDouble(genericRecord.get("total_transaction_amount").toString()));
                windowResult.setTotal_count(Integer.parseInt(genericRecord.get("total_record_count").toString()));
                windowResult.setStartTimeWithString(genericRecord.get("start_time").toString());
                windowResult.setEndTimeWithString(genericRecord.get("end_time").toString());

                ResponseEntityWrapper<WindowResult> entityWrapper = new ResponseEntityWrapper<>();
                entityWrapper.setData(List.of(windowResult));
                entityWrapper.setMessage("Successfully fetched the latest aggregated 1-second period result ");
                return ResponseEntity.ok().body(entityWrapper);
            }
        } catch (Exception e) {
            ResponseEntityWrapper<WindowResult> entityWrapper = new ResponseEntityWrapper<>("An error occurred while processing the request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(entityWrapper);
        }
    }
    @GetMapping("/listen")
    public ResponseEntity<?>listen(){
        hourlyReportService.StartListen();
 ;
        return ResponseEntity.ok().body("ok");
    }

}
