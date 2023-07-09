package com.project.Service;

import com.project.Model.HourlyReport;
import com.project.Payload.DTO.HourlyReportDTO;
import com.project.Repository.HourlyReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HourlyReportService {
    @Autowired
    HourlyReportRepository hourlyReportRepository;
    @Transactional
    public Page<HourlyReportDTO> generateHourlyReports(Date startDateTime, Date endDateTime, Pageable pageable) {
        Page<Object[]> reportData = hourlyReportRepository.getHourlyReportData(startDateTime, endDateTime, pageable);
        List<HourlyReportDTO> hourlyReportDTOS = new ArrayList<>();
        for (Object[] rowData : reportData.getContent()) {
            HourlyReportDTO hourlyReport = new HourlyReportDTO();
            hourlyReport.setHour((int) rowData[0]);
            hourlyReport.setDay((int) rowData[1]);
            hourlyReport.setMonth((int) rowData[2]);
            hourlyReport.setYear((int) rowData[3]);
            hourlyReport.setBalanceAmount((int) rowData[4]);
            hourlyReport.setTransactionCount((int) rowData[5]);

            hourlyReportDTOS.add(hourlyReport);

        }
        return new PageImpl<>(hourlyReportDTOS, reportData.getPageable(), reportData.getTotalElements());

    }
    @Transactional
    public List<HourlyReportDTO> saveHourlyReports(List<HourlyReportDTO> hourlyReportDTOS) {

        for (HourlyReportDTO hourlyReportDTO : hourlyReportDTOS) {
            HourlyReport hourlyReport = new HourlyReport(hourlyReportDTO);
            hourlyReportRepository.save(hourlyReport);
        }
        return hourlyReportDTOS;
    }


}
