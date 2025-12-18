package com.recordroom.recordroom.oldregister.repo;

import com.recordroom.recordroom.oldregister.entity.OldRecordMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OldRecordMasterRepository extends JpaRepository<OldRecordMaster, Integer> {

    @Query(value = "SELECT * FROM old_record_master WHERE record_serial_no = :serial_no AND year = :year LIMIT 1",
            nativeQuery = true)
    Optional<OldRecordMaster> findBySerialNoAndYear(@Param("serial_no") String serialNo,
                                                    @Param("year") Long year);

    @Query(value = "SELECT COUNT(*) FROM old_record_master", nativeQuery = true)
    long countTotalRecords();


    @Query(value = "SELECT COUNT(*) FROM old_record_master WHERE record_serial_no = :serial_no", nativeQuery = true)
    int countByRecordSerialNo(@Param("serial_no") String serialNo);
}