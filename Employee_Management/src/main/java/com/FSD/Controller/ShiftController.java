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

import com.FSD.Entity.ShiftEntity;
import com.FSD.Repository.ShiftRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/shifts")
@Description("Controller for managing work shifts")
public class ShiftController {

    @Autowired
    private ShiftRepository shiftRepository;

    private static final Logger logger = LoggerFactory.getLogger(ShiftController.class);

    @GetMapping
    @Operation(summary = "Get all shifts")
    public List<ShiftEntity> getAllShifts() {
        return shiftRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new shift")
    public ShiftEntity createShift(@RequestBody ShiftEntity shift) {
        return shiftRepository.save(shift);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update shift")
    public ResponseEntity<ShiftEntity> patchShift(@PathVariable Integer id, @RequestBody ShiftEntity shiftDetails) {
        Optional<ShiftEntity> optionalShift = shiftRepository.findById(id);
        if (optionalShift.isPresent()) {
            ShiftEntity shift = optionalShift.get();
            if (shiftDetails.getShiftType() != null) shift.setShiftType(shiftDetails.getShiftType());
            if (shiftDetails.getStartTime() != null) shift.setStartTime(shiftDetails.getStartTime());
            if (shiftDetails.getEndTime() != null) shift.setEndTime(shiftDetails.getEndTime());
            if (shiftDetails.getBreakDuration() != null) shift.setBreakDuration(shiftDetails.getBreakDuration());
            return ResponseEntity.ok(shiftRepository.save(shift));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shift")
    public void deleteShift(@PathVariable Integer id) {
        shiftRepository.deleteById(id);
    }
}