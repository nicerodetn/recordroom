package com.recordroom.recordroom.closed.repo;

import com.recordroom.recordroom.closed.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RecordRepository extends JpaRepository<FileRecord, Integer>, JpaSpecificationExecutor<FileRecord> {

    @Query(
            value = "SELECT * FROM file_record f WHERE f.dr_serial_no = :drSerialNo and f.dr_year =:dryear LIMIT 1",
            nativeQuery = true
    )
    Optional<FileRecord> findByDrSerialNoAndYear(@Param("drSerialNo") Long drSerialNo , @Param("dryear") Integer dryear);

    Optional<FileRecord> findById(Integer id);

    @Query(
            value = "SELECT count(*) FROM file_record",
            nativeQuery = true
    )
    long countClosedFiles();

    @Query(value = """
        SELECT *
        FROM file_record
        WHERE EXTRACT(YEAR FROM AGE(CURRENT_DATE, handing_over_date)) > file_type
        """, nativeQuery = true)
    List<FileRecord> findRecordsExceedingYearLimit();
}