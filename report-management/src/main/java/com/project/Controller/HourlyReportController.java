package com.project.Controller;

import com.project.Assembler.HourlyReportAssembler;
import com.project.Payload.DTO.HourlyReportDTO;
import com.project.Payload.Response.ResponseEntityWrapper;
import com.project.Service.HourlyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController("/api/report")

public class HourlyReportController {
    @Autowired
    HourlyReportService hourlyReportService;

    @Autowired
    HourlyReportAssembler hourlyReportAssembler;

    @GetMapping("/hourlyreport")
    public ResponseEntity<?> getHourlyReport(@RequestParam("startDate") String startDateString ,
                                             @RequestParam("endDate") String endDateString,
                                             @RequestParam(value = "page",required = false) Integer page,
                                             @RequestParam(value = "page",required = false) Integer size
                                             ){

        Page<HourlyReportDTO> hourlyReportDTOS = null;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        Date startDate = null;
        Date endDate = null;

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

        hourlyReportDTOS = hourlyReportService.generateHourlyReports(startDate,endDate,pageable);
        return ResponseEntity.ok().body(hourlyReportAssembler.toCollectionModelwithWrapper(hourlyReportDTOS));

    }
}
