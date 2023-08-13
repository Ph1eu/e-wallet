package com.project.Controller;

import com.project.Assembler.HourlyReportAssembler;
import com.project.Payload.DTO.HourlyReportDTO;
import com.project.Payload.DTO.WindowAggregatedResultDTO;
import com.project.Payload.Response.ResponseEntityWrapper;
import com.project.Payload.Response.ResponsePagedEntityWrapper;
import com.project.Payload.Response.WindowResult;
import com.project.Service.HourlyReportService;
import com.project.Service.WindowAggregatedResultService;
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
    WindowAggregatedResultService windowAggregatedResultService;

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
            Optional<WindowAggregatedResultDTO> result = windowAggregatedResultService.ReceiveLatestRecord();
            if (result.isEmpty()){
               //GenericRecord genericRecord = result.get();
                Date date = new Date();
                ResponseEntityWrapper<WindowResult> entityWrapper = new ResponseEntityWrapper<>("There is no transaction in the system");
                WindowResult windowResult = new WindowResult();
                windowResult.setTotal_amount(0.0);
                windowResult.setTotal_count(0);
                entityWrapper.setData(List.of(windowResult));
                return   ResponseEntity.ok().body(entityWrapper);
            }
            else{
                WindowAggregatedResultDTO windowAggregatedResultDTO = result.get();
                ResponseEntityWrapper<WindowResult> entityWrapper = new ResponseEntityWrapper<>();
                entityWrapper.setMessage("Successfully fetched the latest aggregated result satisfied 1-minute period");
                WindowResult windowResult = new WindowResult();
                windowResult.setTotal_amount(windowAggregatedResultDTO.getTotal_amount());
                windowResult.setTotal_count(windowAggregatedResultDTO.getTotal_count());
                windowResult.setStartTimeWithString(String.valueOf(windowAggregatedResultDTO.getStart_time()));
                windowResult.setEndTimeWithString(String.valueOf(windowAggregatedResultDTO.getEnd_time()));
                entityWrapper.setData(List.of(windowResult));
                return ResponseEntity.ok().body(entityWrapper);
            }
        } catch (Exception e) {
            ResponseEntityWrapper<WindowResult> entityWrapper = new ResponseEntityWrapper<>("An error occurred while processing the request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(entityWrapper);
        }
    }


}
