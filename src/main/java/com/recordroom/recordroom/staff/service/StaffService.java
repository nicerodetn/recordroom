package com.recordroom.recordroom.staff.service;

import com.recordroom.recordroom.staff.entity.Staff;
import com.recordroom.recordroom.staff.repo.StaffRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @PostConstruct
    public void seedInitialStaff() {
        if (staffRepository.count() == 0) {
            Arrays.asList("M. Ramesh", "Balasubramaniyan", "N. Karuppusamy").forEach(name -> {
                Staff s = new Staff();
                s.setName(name);
                s.setActive(true);
                staffRepository.save(s);
            });
        }
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAllByOrderByNameAsc();
    }

    public List<String> getActiveStaffNames() {
        return staffRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(Staff::getName)
                .collect(Collectors.toList());
    }

    public Optional<Staff> getById(Integer id) {
        return staffRepository.findById(id);
    }

    public Staff saveStaff(String name) {
        Staff s = new Staff();
        s.setName(name);
        s.setActive(true);
        return staffRepository.save(s);
    }

    public void updateStaff(Integer id, String name) {
        Optional<Staff> optional = staffRepository.findById(id);
        if (optional.isPresent()) {
            Staff s = optional.get();
            s.setName(name);
            staffRepository.save(s);
        } else {
            throw new RuntimeException("Staff not found");
        }
    }

    public void toggleActive(Integer id) {
        Optional<Staff> optional = staffRepository.findById(id);
        if (optional.isPresent()) {
            Staff s = optional.get();
            s.setActive(!Boolean.TRUE.equals(s.getActive()));
            staffRepository.save(s);
        } else {
            throw new RuntimeException("Staff not found");
        }
    }
}
