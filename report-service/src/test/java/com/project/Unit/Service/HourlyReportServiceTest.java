package com.project.Unit.Service;

import com.project.Payload.DTO.HourlyReportDTO;
import com.project.Repository.HourlyReportRepository;
import com.project.Service.HourlyReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class HourlyReportServiceTest {
    @Mock
    HourlyReportRepository hourlyReportRepository;

    @InjectMocks
    HourlyReportService hourlyReportService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGenerateHourlyReportsByDateTime() {
        // Mocking data
        Date startDate = new Date();
        int startTime = 12;
        Pageable pageable = Pageable.unpaged();

        Object[] rowData = {new BigDecimal(1), new BigDecimal(2), new BigDecimal(3),
                new BigDecimal(2023), new BigDecimal(1000), 10L};
        List<Object[]> reportDataList = new ArrayList<>();
        reportDataList.add(rowData);
        Page<Object[]> reportData = new PageImpl<>(reportDataList, pageable, 1);

        // Mocking repository method
        when(hourlyReportRepository.getHourlyReportDataByDateTime(startDate, startTime, pageable)).thenReturn(reportData);

        // Expected result
        HourlyReportDTO expectedDTO = new HourlyReportDTO();
        expectedDTO.setHour(1);
        expectedDTO.setDay(2);
        expectedDTO.setMonth(3);
        expectedDTO.setYear(2023);
        expectedDTO.setBalanceAmount(1000);
        expectedDTO.setTransactionCount(10);
        List<HourlyReportDTO> expectedDTOS = new ArrayList<>();
        expectedDTOS.add(expectedDTO);
        Page<HourlyReportDTO> expectedPage = new PageImpl<>(expectedDTOS, pageable, 1);

        // Actual result
        Page<HourlyReportDTO> resultPage = hourlyReportService.generateHourlyReportsByDateTime(startDate, startTime, pageable);

        // Verify repository method was called
        verify(hourlyReportRepository, times(1)).getHourlyReportDataByDateTime(startDate, startTime, pageable);

        // Assert the expected and actual results
        assertEquals(expectedPage, resultPage);
}
}