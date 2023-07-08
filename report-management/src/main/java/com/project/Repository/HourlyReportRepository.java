package com.project.Repository;

import com.project.Model.HourlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface HourlyReportRepository extends JpaRepository<HourlyReport, Long> {

    @Modifying
    @Query(value = "INSERT INTO hourly_report (hour, day, month, year, balance_amount, transaction_count) " +
            "SELECT EXTRACT(HOUR FROM transaction_date) AS hour, " +
            "EXTRACT(DAY FROM transaction_date) AS day, " +
            "EXTRACT(MONTH FROM transaction_date) AS month, " +
            "EXTRACT(YEAR FROM transaction_date) AS year, " +
            "SUM(amount) AS balanceAmount, " +
            "COUNT(*) AS transactionCount " +
            "FROM transaction_history " +
            "WHERE transaction_date >= :startDateTime AND transaction_date < :endDateTime " +
            "GROUP BY hour, day, month, year",
            nativeQuery = true)
    @Transactional
    void generateHourlyReports(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime);
}