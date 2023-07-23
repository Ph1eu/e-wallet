package com.project.Payload.Response;

import java.util.Date;

public class WindowResult {
    private Double Total_amount;
    private Integer Total_count;
    private Date Start_time;
    private Date End_time;

    public Double getTotal_amount() {
        return Total_amount;
    }
    public Date convertFromStringtoDate(String timeInMilliseconds){
        long timeInMillis = Long.parseLong(timeInMilliseconds);
        return new Date(timeInMillis);
    }
    public void setStartTimeWithString(String timeInMilliseconds){
        this.Start_time = convertFromStringtoDate(timeInMilliseconds);
    }
    public void setEndTimeWithString(String timeInMilliseconds){
        this.End_time = convertFromStringtoDate(timeInMilliseconds);
    }
    public void setTotal_amount(Double total_amount) {
        Total_amount = total_amount;
    }

    public Integer getTotal_count() {
        return Total_count;
    }

    public void setTotal_count(Integer total_count) {
        Total_count = total_count;
    }

    public Date getStart_time() {
        return Start_time;
    }

    public void setStart_time(Date start_time) {
        Start_time = start_time;
    }

    public Date getEnd_time() {
        return End_time;
    }

    public void setEnd_time(Date end_time) {
        End_time = end_time;
    }
}
