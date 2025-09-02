package com.recordroom.recordroom.Repository;

import com.recordroom.recordroom.Entity.FileRecord;
import com.recordroom.recordroom.Entity.RecordTransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecordTransactionRepository extends JpaRepository<RecordTransactionDetails, Integer> {


    Optional<RecordTransactionDetails> findByDrSerialNo(Long drSerialNo);

    @Query(
            value = "SELECT * FROM record_transaction_details WHERE dr_serial_no = :drSerialNo AND dr_year =:dr_year AND active = true LIMIT 1",
            nativeQuery = true
    )
    Optional<RecordTransactionDetails> findActiveByDrSerialNoAndYear(@Param("drSerialNo") Long drSerialNo,@Param("dr_year") Integer dr_year);

    List<RecordTransactionDetails> findByActiveTrue();


}
