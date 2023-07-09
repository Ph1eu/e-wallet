package com.project.Payload.DTO;

import com.project.Model.HourlyReport;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class HourlyReportDTO {

        private String id;

        private int balanceAmount;

        private int transactionCount;

        private int hour;

        private int day;
        private int month;
        private int year;

    public HourlyReportDTO(String id, int balanceAmount, int transactionCount, int hour, int day, int month, int year) {
        this.id = id;
        this.balanceAmount = balanceAmount;
        this.transactionCount = transactionCount;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public HourlyReportDTO() {
    }

    public HourlyReportDTO(HourlyReport hourlyReport){
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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
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
