package com.project.service;

import com.project.model.WindowAggregatedResult;
import com.project.payload.dto.WindowAggregatedResultDTO;
import com.project.repository.WindowAggregatedResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class WindowAggregatedResultService {
    @Autowired
    WindowAggregatedResultRepository windowAggregatedResultRepository;

    public void insertRecord(WindowAggregatedResultDTO windowAggregatedResultDTO) {
        windowAggregatedResultRepository.insertRecord(windowAggregatedResultDTO.getId(), windowAggregatedResultDTO.getTotal_amount(),
                windowAggregatedResultDTO.getTotal_count(), windowAggregatedResultDTO.getStart_time(),
                windowAggregatedResultDTO.getEnd_time());
    }

    public WindowAggregatedResultDTO retrieveLatestRecord() {
        WindowAggregatedResult windowAggregatedResult = windowAggregatedResultRepository.findLatestSingleRecord();
        WindowAggregatedResultDTO windowAggregatedResultDTO = new WindowAggregatedResultDTO(windowAggregatedResult.getId(),
                windowAggregatedResult.getTotal_amount(),
                windowAggregatedResult.getTotal_count(),
                windowAggregatedResult.getStart_time(),
                windowAggregatedResult.getEnd_time());
        return windowAggregatedResultDTO;
    }

    public Optional<WindowAggregatedResultDTO> ReceiveLatestRecord() {
        Date date = new Date();
        WindowAggregatedResult windowAggregatedResult = windowAggregatedResultRepository.findLatestSingleRecord();
        //  GenericRecord record = aggregatedTransactionConsumer.getLatestRecord();
        //  System.out.println(record);
        long timeIntervalMillis = 30 * 1000;
        long intervalStart = date.getTime() - timeIntervalMillis;
        long intervalEnd = date.getTime() + timeIntervalMillis;
        boolean isRecordTimeInInterval = windowAggregatedResult.getStart_time() >= intervalStart && windowAggregatedResult.getEnd_time() <= intervalEnd;
        if (isRecordTimeInInterval) {
            System.out.println("record is in interval");
            WindowAggregatedResultDTO windowAggregatedResultDTO = new WindowAggregatedResultDTO(windowAggregatedResult.getId(),
                    windowAggregatedResult.getTotal_amount(),
                    windowAggregatedResult.getTotal_count(),
                    windowAggregatedResult.getStart_time(),
                    windowAggregatedResult.getEnd_time());
            System.out.println(windowAggregatedResultDTO);
            return Optional.of(windowAggregatedResultDTO);
        } else {
            System.out.println("record is out of interval");
            System.out.println(windowAggregatedResult);
            return Optional.empty();
        }
    }
}
