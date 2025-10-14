package com.recordroom.recordroom.dashbord;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface DashboardSnapshotRepository extends JpaRepository<DashboardSnapshot, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM DashboardSnapshot d WHERE d.snapshotDate = :date")
    void deleteBySnapshotDate(LocalDate date);

    DashboardSnapshot findBySnapshotDate(LocalDate date);

}
