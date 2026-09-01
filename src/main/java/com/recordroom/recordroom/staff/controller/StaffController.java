package com.recordroom.recordroom.staff.controller;

import com.recordroom.recordroom.staff.entity.Staff;
import com.recordroom.recordroom.staff.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/staff_management")
    public String staffManagement(Model model) {
        model.addAttribute("staffMembers", staffService.getAllStaff());
        return "fragments/staff/staff_management :: content";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveStaff(@RequestParam("name") String name) {
        staffService.saveStaff(name);
        return ResponseEntity.ok("Staff saved successfully");
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateStaff(@RequestParam("id") Integer id,
                                               @RequestParam("name") String name) {
        try {
            staffService.updateStaff(id, name);
            return ResponseEntity.ok("Staff updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating staff.");
        }
    }

    @PostMapping("/toggle/{id}")
    @ResponseBody
    public ResponseEntity<String> toggleActive(@PathVariable Integer id) {
        try {
            staffService.toggleActive(id);
            return ResponseEntity.ok("Staff status updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating staff status.");
        }
    }
}
