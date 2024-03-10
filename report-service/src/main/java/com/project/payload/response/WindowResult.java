package com.project.payload.response;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class WindowResult {
    private Double Total_amount;
    private Integer Total_count;
    private String Start_time;
    private String End_time;

    public Double getTotal_amount() {
        return Total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        Total_amount = total_amount;
    }

    public String convertFromStringtoDate(String timeInMilliseconds) {
        long timeInMillis = Long.parseLong(timeInMilliseconds);
        LocalDateTime localDateTime = Instant.ofEpochMilli(timeInMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return localDateTime.format(formatter);
    }

    public void setStartTimeWithString(String timeInMilliseconds) {

        this.Start_time = convertFromStringtoDate(timeInMilliseconds);
    }

    public void setEndTimeWithString(String timeInMilliseconds) {
        this.End_time = convertFromStringtoDate(timeInMilliseconds);
    }

    public Integer getTotal_count() {
        return Total_count;
    }

    public void setTotal_count(Integer total_count) {
        Total_count = total_count;
    }

    public String getStart_time() {
        return Start_time;
    }

    public void setStart_time(String start_time) {
        Start_time = start_time;
    }

    public String getEnd_time() {
        return End_time;
    }

    public void setEnd_time(String end_time) {
        End_time = end_time;
    }
}
