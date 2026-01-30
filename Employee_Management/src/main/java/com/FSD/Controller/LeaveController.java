package com.FSD.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FSD.Entity.EmployeeEntity;
import com.FSD.Entity.LeaveEntity;
import com.FSD.Entity.LeaveTypeEntity;
import com.FSD.Repository.EmployeeRepository;
import com.FSD.Repository.LeaveRepository;
import com.FSD.Repository.LeaveTypeRepository;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    // GET ALL
    @GetMapping
    public List<LeaveEntity> getAllLeaves() {
        return leaveRepository.findAll();
    }

    // APPLY (CREATE)
    @PostMapping("/apply")
    public LeaveEntity applyLeave(@RequestBody LeaveRequest request) {
        // 1. Fetch Employee
        EmployeeEntity employee = employeeRepository.findById(request.employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + request.employeeId));

        // 2. Fetch Leave Type
        LeaveTypeEntity leaveType = leaveTypeRepository.findById(request.leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave Type not found with ID: " + request.leaveTypeId));

        // 3. Create and Save Leave
        LeaveEntity leave = new LeaveEntity();
        leave.setEmployee(employee);
        leave.setLeaveType(leaveType);
        leave.setLeaveCount(request.leaveCount);
        leave.setApproved(false); // Default to Pending

        return leaveRepository.save(leave);
    }

    // APPROVE (UPDATE)
    @PutMapping("/{id}/approve")
    public LeaveEntity approveLeave(@PathVariable Long id) {
        LeaveEntity leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with ID: " + id));
        
        leave.setApproved(true);
        return leaveRepository.save(leave);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteLeave(@PathVariable Long id) {
        if (!leaveRepository.existsById(id)) {
            throw new RuntimeException("Leave request not found with ID: " + id);
        }
        leaveRepository.deleteById(id);
        return "Leave request deleted successfully.";
    }

    // Helper DTO class for incoming JSON
    public static class LeaveRequest {
        public Integer employeeId;
        public Long leaveTypeId;
        public int leaveCount;
    }
}