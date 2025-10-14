package com.recordroom.recordroom.reference.repo;

import com.recordroom.recordroom.callbook.entity.CallBook;
import com.recordroom.recordroom.closed.entity.FileRecord;
import com.recordroom.recordroom.reference.entity.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ReferenceRepo extends JpaRepository<Reference, Integer> {

    @Query(
            value = "SELECT * FROM reference f WHERE f.serial_no = :serial_no",
            nativeQuery = true
    )
    Optional<Reference> findByDrSerialNo(@Param("serial_no") Long serial_no);

    @Query(
            value = "SELECT * FROM reference f WHERE f.is_active = true",
            nativeQuery = true
    )
    List<Reference> findAllActive();


}
