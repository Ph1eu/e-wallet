package com.project.Service;

import com.project.Kafka.Consumer.AggregatedTransactionConsumer;
import com.project.Kafka.Producer.TransactionProducer;
import com.project.Kafka.Stream.GlobalAggregation;
import com.project.Kafka.Stream.OneSecAggregation;
import com.project.Model.HourlyReport;
import com.project.Model.TransactionHistory;
import com.project.Payload.DTO.HourlyReportDTO;
import com.project.Repository.HourlyReportRepository;
import com.project.Repository.TransactionHistoryRepository;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HourlyReportService {
    @Autowired
    HourlyReportRepository hourlyReportRepository;
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    TransactionProducer transactionProducer;
    @Autowired
    AggregatedTransactionConsumer aggregatedTransactionConsumer;
    @Autowired
    GlobalAggregation globalAggregation;
    @Autowired
    OneSecAggregation oneSecAggregation;

    private List<HourlyReportDTO> mapReportDataToList(List<Object[]> reportData) {
        List<HourlyReportDTO> hourlyReportDTOS = new ArrayList<>();
        for (Object[] rowData : reportData) {
            HourlyReportDTO hourlyReport = new HourlyReportDTO();
            hourlyReport.setHour(((BigDecimal) rowData[0]).intValue());
            hourlyReport.setDay(((BigDecimal) rowData[1]).intValue());
            hourlyReport.setMonth(((BigDecimal) rowData[2]).intValue());
            hourlyReport.setYear(((BigDecimal) rowData[3]).intValue());
            hourlyReport.setBalanceAmount(((BigDecimal) rowData[4]).intValue());
            hourlyReport.setTransactionCount(((Long) rowData[5]).intValue());

            hourlyReportDTOS.add(hourlyReport);
        }
        return hourlyReportDTOS;
    }
    public Page<HourlyReportDTO> generateHourlyReportsByDateTime(Date startDate,int startTime, Pageable pageable) {
        Page<Object[]> reportData = hourlyReportRepository.getHourlyReportDataByDateTime(startDate,startTime, pageable);
        List<HourlyReportDTO> hourlyReportDTOS = mapReportDataToList(reportData.getContent());
        return new PageImpl<>(hourlyReportDTOS, reportData.getPageable(), reportData.getTotalElements());
    }
    public Page<HourlyReportDTO> generateHourlyReportsByDate(Date startDate, Pageable pageable) {
        Page<Object[]> reportData = hourlyReportRepository.getHourlyReportDataByDate(startDate, pageable);
        List<HourlyReportDTO> hourlyReportDTOS = mapReportDataToList(reportData.getContent());
        return new PageImpl<>(hourlyReportDTOS, reportData.getPageable(), reportData.getTotalElements());
    }
    public Page<HourlyReportDTO> generateHourlyReports(Date startDateTime, Date endDateTime, Pageable pageable) {
        Page<Object[]> reportData = hourlyReportRepository.getHourlyReportData(startDateTime, endDateTime, pageable);
        List<HourlyReportDTO> hourlyReportDTOS = mapReportDataToList(reportData.getContent());
        return new PageImpl<>(hourlyReportDTOS, reportData.getPageable(), reportData.getTotalElements());
    }

    public List<HourlyReportDTO> saveHourlyReports(List<HourlyReportDTO> hourlyReportDTOS) {

        for (HourlyReportDTO hourlyReportDTO : hourlyReportDTOS) {
            HourlyReport hourlyReport = new HourlyReport(hourlyReportDTO);
            hourlyReportRepository.save(hourlyReport);
        }
        return hourlyReportDTOS;
    }
    public void StreamTransactions() {
//        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();
//
//        for (TransactionHistory transaction : transactionHistoryList) {
//
//            transactionProducer.publish(transaction.getId(), transaction);
//
//        }
        oneSecAggregation.startStream();

    }
    public void PublishTransactions() {
        List<TransactionHistory> transactionHistoryList = transactionHistoryRepository.findAll();

        for (TransactionHistory transaction : transactionHistoryList) {

            transactionProducer.publish(transaction);
        }

    }

}
