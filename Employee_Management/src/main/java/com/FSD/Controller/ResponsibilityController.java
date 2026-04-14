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

import com.FSD.Entity.ResponsibilityEntity;
import com.FSD.Repository.ResponsibilityRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/api/responsibilities")
@Description("Controller for managing responsibilities")
public class ResponsibilityController {

    @Autowired
    private ResponsibilityRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(ResponsibilityController.class);

    @GetMapping
    public List<ResponsibilityEntity> getAll() { return repo.findAll(); }

    @PostMapping
    public ResponsibilityEntity create(@RequestBody ResponsibilityEntity entity) {
        return repo.save(entity);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update responsibility")
    public ResponseEntity<ResponsibilityEntity> patch(@PathVariable Integer id, @RequestBody ResponsibilityEntity details) {
        Optional<ResponsibilityEntity> opt = repo.findById(id);
        if (opt.isPresent()) {
            ResponsibilityEntity ent = opt.get();
            
            // ✅ Handle Project Update
            if (details.getProject() != null) {
                ent.setProject(details.getProject());
            }
            // Handle Type Update
            if (details.getResponsibilityType() != null) {
                ent.setResponsibilityType(details.getResponsibilityType());
            }
            // Handle Clearance Update
            if (details.getClearanceLevel() != null) {
                ent.setClearanceLevel(details.getClearanceLevel());
            }
            // Handle Dates
            if (details.getStartDate() != null) ent.setStartDate(details.getStartDate());
            if (details.getEndDate() != null) ent.setEndDate(details.getEndDate());

            return ResponseEntity.ok(repo.save(ent));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        repo.deleteById(id);
    }
}