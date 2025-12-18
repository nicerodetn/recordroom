package com.recordroom.recordroom.oldregister.repo;

import com.recordroom.recordroom.oldregister.entity.DistrictMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictMasterRepository extends JpaRepository<DistrictMaster, Integer> {


    @Query("SELECT DISTINCT d.talukName FROM DistrictMaster d ORDER BY d.talukName ASC")
    List<String> findDistinctTalukNames();


    @Query("SELECT d.villageName FROM DistrictMaster d WHERE d.talukName = :talukName ORDER BY d.villageName ASC")
    List<String> findVillagesByTaluk(@Param("talukName") String talukName);
}