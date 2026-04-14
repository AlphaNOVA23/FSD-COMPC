package com.FSD.Controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FSD.Entity.TimesheetEntity;
import com.FSD.Repository.TimesheetRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/api/timesheets")
@Description("Controller for managing timesheets")
public class TimesheetController {

    @Autowired
    private TimesheetRepository timesheetRepository;

    private static final Logger logger = LoggerFactory.getLogger(TimesheetController.class);

    @GetMapping
    @Operation(summary = "Get all timesheets")
    public List<TimesheetEntity> getAllTimesheets() {
        return timesheetRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new timesheet")
    public TimesheetEntity createTimesheet(@RequestBody TimesheetEntity timesheet) {
        return timesheetRepository.save(timesheet);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update timesheet")
    public ResponseEntity<TimesheetEntity> patchTimesheet(@PathVariable Integer id, @RequestBody TimesheetEntity details) {
        Optional<TimesheetEntity> optionalTs = timesheetRepository.findById(id);
        if (optionalTs.isPresent()) {
            TimesheetEntity ts = optionalTs.get();
            if (details.getCheckIn() != null) ts.setCheckIn(details.getCheckIn());
            if (details.getCheckOut() != null) ts.setCheckOut(details.getCheckOut());
            if (details.getTotalHours() != null) ts.setTotalHours(details.getTotalHours());
            if (details.getStatus() != null) ts.setStatus(details.getStatus());
            if (details.getRemarks() != null) ts.setRemarks(details.getRemarks());
            return ResponseEntity.ok(timesheetRepository.save(ts));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete timesheet")
    public void deleteTimesheet(@PathVariable Integer id) {
        timesheetRepository.deleteById(id);
    }
}