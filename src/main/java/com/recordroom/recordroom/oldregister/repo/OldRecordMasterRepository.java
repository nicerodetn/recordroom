package com.recordroom.recordroom.oldregister.repo;

import com.recordroom.recordroom.oldregister.entity.OldRecordMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface OldRecordMasterRepository extends JpaRepository<OldRecordMaster, Integer>, JpaSpecificationExecutor<OldRecordMaster> {

    @Query(value = "SELECT * FROM old_record_master WHERE record_serial_no = :serial_no AND year = :year LIMIT 1",
            nativeQuery = true)
    Optional<OldRecordMaster> findBySerialNoAndYear(@Param("serial_no") Long serialNo,
                                                    @Param("year") Long year);

    @Query(value = "SELECT COUNT(*) FROM old_record_master", nativeQuery = true)
    long countTotalRecords();


    // to find all unique record types ( 'RSR', 'SLR', 'SR')
    @Query("SELECT DISTINCT o.types_of_record FROM OldRecordMaster o WHERE o.types_of_record IS NOT NULL AND o.types_of_record != '' ORDER BY o.types_of_record")
    List<String> findDistinctRecordTypes();
}