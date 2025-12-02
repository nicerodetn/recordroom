package com.recordroom.recordroom.callbook.repo;


import com.recordroom.recordroom.callbook.entity.CallBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CallBookRepository extends JpaRepository<CallBook, Integer>, JpaSpecificationExecutor<CallBook> {

    @Query(
            value = "SELECT * FROM call_book f WHERE f.new_dr_serial_no = :new_dr_serial_no and f.new_dr_year =:new_dr_year AND is_current = true AND in_out_status =0 LIMIT 1",
            nativeQuery = true
    )
    Optional<CallBook> findByDrSerialNoAndYear(@Param("new_dr_serial_no") Integer new_dr_serial_no , @Param("new_dr_year") Integer new_dr_year);


    @Query(
            value = "SELECT * FROM call_book f WHERE f.new_dr_serial_no = :new_dr_serial_no and f.new_dr_year =:new_dr_year",
            nativeQuery = true
    )
    Optional<CallBook> findForDuplicate(@Param("new_dr_serial_no") Integer new_dr_serial_no , @Param("new_dr_year") Integer new_dr_year);


    @Query(
            value = "SELECT * FROM call_book f WHERE f.new_dr_serial_no = :new_dr_serial_no and f.new_dr_year =:new_dr_year AND is_current = true AND in_out_status =1 LIMIT 1",
            nativeQuery = true
    )
    Optional<CallBook> findByDrSerialNoAndYearForINward(@Param("new_dr_serial_no") Integer new_dr_serial_no , @Param("new_dr_year") Integer new_dr_year);

    @Query(
            value = "SELECT * FROM call_book f WHERE f.unique_key = :unique_key",
            nativeQuery = true
    )
    List<CallBook> findByUniqueKey(@Param("unique_key") Integer unique_key);

    @Query(
            value = "SELECT * FROM call_book f WHERE f.is_current = true",
            nativeQuery = true
    )
    List<CallBook> findByActive();

    @Query(
            value = "SELECT count(*) FROM call_book f WHERE f.is_current = true and f.in_out_status=1",
            nativeQuery = true
    )
    long countCallBookOutstanding();

    @Query(
            value = "SELECT count(*) FROM call_book f WHERE f.is_current = true",
            nativeQuery = true
    )
    long countCallBookTotal();

}