package com.project.Unit.Service;

import com.project.Repository.HourlyReportRepository;
import com.project.Service.HourlyReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

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
    void generateHourlyReports() {
    }
}