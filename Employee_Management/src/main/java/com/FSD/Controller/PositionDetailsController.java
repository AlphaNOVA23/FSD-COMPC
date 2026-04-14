package com.FSD.Controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FSD.Entity.PositionDetailsEntity;
import com.FSD.Repository.PositionDetailsRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/api/positions")
@Description("Controller for managing position details")
public class PositionDetailsController {

    @Autowired
    private PositionDetailsRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(PositionDetailsController.class);

    @GetMapping("/{id}")
    @Operation(summary = "Get position by Employee ID")
    public ResponseEntity<PositionDetailsEntity> getPosition(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create or update position")
    public PositionDetailsEntity createPosition(@RequestBody PositionDetailsEntity position) {
        return repo.save(position);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update position")
    public ResponseEntity<PositionDetailsEntity> patchPosition(@PathVariable Integer id, @RequestBody PositionDetailsEntity details) {
        Optional<PositionDetailsEntity> optionalPos = repo.findById(id);
        if (optionalPos.isPresent()) {
            PositionDetailsEntity pos = optionalPos.get();
            if (details.getTitle() != null) pos.setTitle(details.getTitle());
            if (details.getJobLevel() != null) pos.setJobLevel(details.getJobLevel());
            if (details.getSalaryGrade() != null) pos.setSalaryGrade(details.getSalaryGrade());
            if (details.getBaseSalary() != null) pos.setBaseSalary(details.getBaseSalary());
            if (details.getCurrency() != null) pos.setCurrency(details.getCurrency());
            return ResponseEntity.ok(repo.save(pos));
        }
        return ResponseEntity.notFound().build();
    }
}