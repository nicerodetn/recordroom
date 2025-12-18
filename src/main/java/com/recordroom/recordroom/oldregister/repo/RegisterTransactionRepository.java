package com.recordroom.recordroom.oldregister.repo;

import com.recordroom.recordroom.oldregister.entity.RegisterTransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegisterTransactionRepository extends JpaRepository<RegisterTransactionDetails, Integer> {


    @Query(value = "SELECT rtd.* FROM register_transaction_details rtd " +
            "JOIN old_record_master orm ON rtd.master_id = orm.id " +
            "WHERE orm.record_serial_no = :serial_no " +
            "AND orm.year = :year " +
            "AND rtd.is_active_status = true " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<RegisterTransactionDetails> findActiveBySerialNoAndYear(@Param("serial_no") String serialNo,
                                                                     @Param("year") Long year);

    @Query(value = "SELECT * FROM register_transaction_details WHERE is_active_status = true",
            nativeQuery = true)
    List<RegisterTransactionDetails> findAllActiveTransactions();

    @Query(value = "SELECT * FROM register_transaction_details WHERE is_active_status = false AND date_of_return IS NOT NULL",
            nativeQuery = true)
    List<RegisterTransactionDetails> findAllCompletedTransactions();

    @Query(value = "SELECT COUNT(*) FROM register_transaction_details WHERE is_active_status = true",
            nativeQuery = true)
    long countActiveTransactions();
}