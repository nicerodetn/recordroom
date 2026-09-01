package com.recordroom.recordroom.staff.repo;

import com.recordroom.recordroom.staff.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

    List<Staff> findByActiveTrueOrderByNameAsc();

    List<Staff> findAllByOrderByNameAsc();
}
