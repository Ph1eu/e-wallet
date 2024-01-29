package com.project.payload.dto;

public class WindowAggregatedResultDTO {private String id ;
    private  Double total_amount;
    private Integer total_count;
    private Long  start_time;
    private Long end_time;

    public WindowAggregatedResultDTO(String id, Double total_amount, Integer total_count, Long start_time, Long end_time) {
        this.id = id;
        this.total_amount = total_amount;
        this.total_count = total_count;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public WindowAggregatedResultDTO(Double total_amount, Integer total_count, Long start_time, Long end_time) {
        this.total_amount = total_amount;
        this.total_count = total_count;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    @Override
    public String toString() {
        return "WindowAggregatedResultDTO{" +
                "id='" + id + '\'' +
                ", total_amount=" + total_amount +
                ", total_count=" + total_count +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                '}';
    }
}
