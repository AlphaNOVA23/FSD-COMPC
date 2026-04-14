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

import com.FSD.Entity.SalaryEntity;
import com.FSD.Repository.SalaryRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/api/salaries")
@Description("Controller for managing salary entities")
public class SalaryController {

    @Autowired
    private SalaryRepository salaryRepository;

    private static final Logger logger = LoggerFactory.getLogger(SalaryController.class);

    @GetMapping
    @Operation(summary = "Get all salaries", description = "Retrieves a list of all salary records")
    public List<SalaryEntity> getAllSalaries() {
        logger.info("Request received to get all salaries");
        return salaryRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get salary by ID", description = "Retrieves a salary entity by its ID")
    public ResponseEntity<SalaryEntity> getSalaryById(@PathVariable Integer id) {
        logger.info("Request received to get salary with ID: {}", id);
        return salaryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new salary record", description = "Creates a new salary entity")
    public SalaryEntity createSalary(@RequestBody SalaryEntity salary) {
        logger.info("Request received to create a new salary record");
        // Ensure net salary is calculated
        salary.calculateNetSalary();
        return salaryRepository.save(salary);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update salary", description = "Updates specific fields of an existing salary entity")
    public ResponseEntity<SalaryEntity> patchSalary(@PathVariable Integer id, @RequestBody SalaryEntity salaryDetails) {
        logger.info("Request received to patch salary with ID: {}", id);
        Optional<SalaryEntity> optionalSalary = salaryRepository.findById(id);

        if (optionalSalary.isPresent()) {
            SalaryEntity salary = optionalSalary.get();

            if (salaryDetails.getEmployee() != null) {
                salary.setEmployee(salaryDetails.getEmployee());
            }
            if (salaryDetails.getBaseSalary() != null) {
                salary.setBaseSalary(salaryDetails.getBaseSalary());
            }
            if (salaryDetails.getBonus() != null) {
                salary.setBonus(salaryDetails.getBonus());
            }
            if (salaryDetails.getDeductions() != null) {
                salary.setDeductions(salaryDetails.getDeductions());
            }
            
            // Recalculate net salary after updates
            salary.calculateNetSalary();
            
            return ResponseEntity.ok(salaryRepository.save(salary));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete salary by ID", description = "Deletes a salary entity by its ID")
    public void deleteSalary(@PathVariable Integer id) {
        logger.info("Request received to delete salary with ID: {}", id);
        salaryRepository.deleteById(id);
    }
}