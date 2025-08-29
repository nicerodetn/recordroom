package com.recordroom.recordroom.Repository;

import com.recordroom.recordroom.Entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordRepository extends JpaRepository<FileRecord, Integer> {


    Optional<FileRecord> findByDrSerialNo(Long drSerialNo);

    Optional<FileRecord> findById(Integer id);
}
