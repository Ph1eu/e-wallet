package com.project.model;

import com.project.payload.dto.HourlyReportDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "hourly_report")
public class HourlyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "balance_amount")
    private int balanceAmount;

    @Column(name = "transaction_count")
    private int transactionCount;

    @Column(name = "hour")
    private int hour;
    @Column(name = "day")
    private int day;

    @Column(name = "month")
    private int month;

    @Column(name = "year")
    private int year;

    public HourlyReport() {
    }

    public HourlyReport(String id, int balanceAmount, int transactionCount, int hour, int day, int month, int year) {
        this.id = id;
        this.balanceAmount = balanceAmount;
        this.transactionCount = transactionCount;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public HourlyReport(HourlyReportDTO hourlyReport) {
        this.id = hourlyReport.getId();
        this.balanceAmount = hourlyReport.getBalanceAmount();
        this.transactionCount = hourlyReport.getTransactionCount();
        this.hour = hourlyReport.getHour();
        this.day = hourlyReport.getDay();
        this.month = hourlyReport.getMonth();
        this.year = hourlyReport.getYear();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(int balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}