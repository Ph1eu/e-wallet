package com.project.Repository;

import com.project.Model.WindowAggregatedResult;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WindowAggregatedResultRepository extends JpaRepository<WindowAggregatedResult,String> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO window_aggregated_result (id, total_amount, total_count, start_time, end_time) " +
            "VALUES (:id, :totalAmount, :totalCount, :startTime, :endTime)", nativeQuery = true)
    void insertRecord(
            @Param("id") String id,
            @Param("totalAmount") Double totalAmount,
            @Param("totalCount") Integer totalCount,
            @Param("startTime") Long startTime,
            @Param("endTime") Long endTime
    );
    // You can also use the following query to retrieve just a single record (the latest one):
    @Query("SELECT ar FROM WindowAggregatedResult ar ORDER BY ar.end_time DESC LIMIT 1")
    WindowAggregatedResult findLatestSingleRecord();
}
