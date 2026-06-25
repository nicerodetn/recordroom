package com.recordroom.recordroom.rti.repo;


import com.recordroom.recordroom.rti.entity.Rti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RtiRepository extends JpaRepository<Rti, Integer> {

    @Query(
            value = "SELECT count(*) FROM rti WHERE is_deleted = false",
            nativeQuery = true
    )
    long countAllRti();

    // Filter by BOTH Month and Year
    @Query("SELECT r FROM Rti r WHERE YEAR(r.rtiDate) = :year AND MONTH(r.rtiDate) = :month AND r.isDeleted = false")
    List<Rti> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    // Filter by Year ONLY
    @Query("SELECT r FROM Rti r WHERE YEAR(r.rtiDate) = :year AND r.isDeleted = false")
    List<Rti> findByYear(@Param("year") int year);

    // Find all not deleted
    @Query("SELECT r FROM Rti r WHERE r.isDeleted = false")
    List<Rti> findAllNotDeleted();

    // Find all deleted
    @Query("SELECT r FROM Rti r WHERE r.isDeleted = true")
    List<Rti> findAllDeleted();

}
