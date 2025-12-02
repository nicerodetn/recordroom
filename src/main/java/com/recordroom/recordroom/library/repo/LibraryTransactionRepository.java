package com.recordroom.recordroom.library.repo;

import com.recordroom.recordroom.library.entity.LibraryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface LibraryTransactionRepository extends JpaRepository<LibraryTransaction, Integer>, JpaSpecificationExecutor<LibraryTransaction> {

    @Query("SELECT lt FROM LibraryTransaction lt WHERE lt.library.id = :#{#libraryId} AND lt.date_of_return IS NULL")
    Optional<LibraryTransaction> findByLibraryIdAndDateOfReturnIsNull(@Param("libraryId") Integer libraryId);

    @Query("SELECT lt FROM LibraryTransaction lt WHERE lt.date_of_return IS NULL")
    List<LibraryTransaction> findByDate_of_returnIsNull();

    @Query("SELECT lt FROM LibraryTransaction lt WHERE lt.library.book_serial_no = :#{#serialNo} AND lt.date_of_return IS NULL")
    Optional<LibraryTransaction> findActiveTransactionByBookSerialNo(@Param("serialNo") Integer serialNo);

    @Query("SELECT lt FROM LibraryTransaction lt JOIN FETCH lt.library WHERE lt.date_of_return IS NOT NULL")
    List<LibraryTransaction> findReturnedTransactions();

}