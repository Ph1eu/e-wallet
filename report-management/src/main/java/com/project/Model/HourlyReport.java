package com.project.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "hourly_report")
public class HourlyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

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

    public HourlyReport(Long id, int balanceAmount, int transactionCount, int hour, int day, int month, int year) {
        this.id = id;
        this.balanceAmount = balanceAmount;
        this.transactionCount = transactionCount;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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