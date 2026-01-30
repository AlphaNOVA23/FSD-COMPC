package com.FSD.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FSD.Entity.LeaveTypeEntity;
import com.FSD.Repository.LeaveTypeRepository;

@RestController
@RequestMapping("/api/leave-types")
public class LeaveTypeController {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    // 1. GET ALL LEAVE TYPES
    @GetMapping
    public List<LeaveTypeEntity> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }

    // 2. CREATE NEW LEAVE TYPE
    @PostMapping
    public LeaveTypeEntity createLeaveType(@RequestBody LeaveTypeEntity leaveType) {
        return leaveTypeRepository.save(leaveType);
    }

    // 3. DELETE LEAVE TYPE
    @DeleteMapping("/{id}")
    public String deleteLeaveType(@PathVariable Long id) {
        if (!leaveTypeRepository.existsById(id)) {
            throw new RuntimeException("Leave Type not found with ID: " + id);
        }
        leaveTypeRepository.deleteById(id);
        return "Leave Type deleted successfully.";
    }
}