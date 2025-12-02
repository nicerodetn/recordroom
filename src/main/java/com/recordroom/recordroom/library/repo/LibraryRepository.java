package com.recordroom.recordroom.library.repo;

import com.recordroom.recordroom.library.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LibraryRepository extends JpaRepository<Library, Integer>, JpaSpecificationExecutor<Library> {

    @Query("SELECT l FROM Library l WHERE l.book_serial_no = :#{#bookSerialNo}")
    Optional<Library> findByBook_serial_no(@Param("bookSerialNo") Integer bookSerialNo);

}