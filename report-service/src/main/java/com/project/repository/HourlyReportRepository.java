package com.project.repository;

import com.project.model.HourlyReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT EXTRACT(HOUR FROM transaction_date) AS hour, " +
            "EXTRACT(DAY FROM transaction_date) AS day, " +
            "EXTRACT(MONTH FROM transaction_date) AS month, " +
            "EXTRACT(YEAR FROM transaction_date) AS year, " +
            "SUM(amount) AS balanceAmount, " +
            "COUNT(*) AS transactionCount " +
            "FROM transaction_history " +
            "WHERE transaction_date >= :startDateTime AND transaction_date < :endDateTime " +
            "GROUP BY hour, day, month, year", nativeQuery = true)
    Page<Object[]> getHourlyReportData(@Param("startDateTime") Date startDateTime, @Param("endDateTime") Date endDateTime, Pageable pageable);

    @Query(value = "SELECT EXTRACT(HOUR FROM transaction_date) AS hour, " +
            "EXTRACT(DAY FROM transaction_date) AS day, " +
            "EXTRACT(MONTH FROM transaction_date) AS month, " +
            "EXTRACT(YEAR FROM transaction_date) AS year, " +
            "SUM(amount) AS balanceAmount, " +
            "COUNT(*) AS transactionCount " +
            "FROM transaction_history " +
            "WHERE DATE(transaction_date) = :startDate " +
            "GROUP BY hour, day, month, year", nativeQuery = true)
    Page<Object[]> getHourlyReportDataByDate(@Param("startDate") Date startDate, Pageable pageable);

    @Query(value = "SELECT EXTRACT(HOUR FROM transaction_date) AS hour, " +
            "EXTRACT(DAY FROM transaction_date) AS day, " +
            "EXTRACT(MONTH FROM transaction_date) AS month, " +
            "EXTRACT(YEAR FROM transaction_date) AS year, " +
            "SUM(amount) AS balanceAmount, " +
            "COUNT(*) AS transactionCount " +
            "FROM transaction_history " +
            "WHERE DATE(transaction_date) = :startDate " +
            "AND EXTRACT(HOUR FROM transaction_date) >= :startHour " +
            "AND EXTRACT(HOUR FROM transaction_date) < (:startHour + 24) " +
            "GROUP BY hour, day, month, year", nativeQuery = true)
    Page<Object[]> getHourlyReportDataByDateTime(@Param("startDate") Date startDate, @Param("startHour") int startHour, Pageable pageable);
}