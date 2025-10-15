package com.recordroom.recordroom.rti.repo;


import com.recordroom.recordroom.rti.entity.Rti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RtiRepository extends JpaRepository<Rti, Integer> {




    @Query(
            value = "SELECT count(*) FROM rti",
            nativeQuery = true
    )
    long countAllRti();
}
