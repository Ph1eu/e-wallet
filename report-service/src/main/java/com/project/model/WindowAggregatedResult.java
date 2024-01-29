package com.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "window_aggregated_result")
@Immutable
public class WindowAggregatedResult {
    @Id
    @Column(name = "id",unique = true)
    private String id ;
    @Column(name="total_amount")
    private  Double total_amount;
    @Column(name = "total_count")
    private Integer total_count;
    @Column(name = "start_time",unique = true)
    private Long  start_time;
    @Column(name = "end_time")
    private Long end_time;

    public WindowAggregatedResult(Double total_amount, Integer total_count, Long start_time, Long end_time) {
        this.total_amount = total_amount;
        this.total_count = total_count;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public WindowAggregatedResult(String id, Double total_amount, Integer total_count, Long start_time, Long end_time) {
        this.id = id;
        this.total_amount = total_amount;
        this.total_count = total_count;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public WindowAggregatedResult() {
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
        return "WindowAggregatedResult{" +
                "id='" + id + '\'' +
                ", total_amount=" + total_amount +
                ", total_count=" + total_count +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                '}';
    }
}
