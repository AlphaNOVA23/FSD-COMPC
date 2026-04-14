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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FSD.Entity.DepartmentHeadEntity;
import com.FSD.Repository.DepartmentHeadRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/api/department-heads")
@Description("Controller for managing department head entities")
public class DepartmentHeadController {

    @Autowired
    private DepartmentHeadRepository departmentHeadRepository;

    private static final Logger logger = LoggerFactory.getLogger(DepartmentHeadController.class);

    @GetMapping
    @Operation(summary = "Get all department heads", description = "Retrieves a list of all department head entities")
    public List<DepartmentHeadEntity> getAllDepartmentHeads() {
        logger.info("Request received to get all department heads");
        return departmentHeadRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department head by ID", description = "Retrieves a department head entity by its ID")
    public ResponseEntity<DepartmentHeadEntity> getDepartmentHeadById(@PathVariable("id") Integer id) {
        logger.info("Request received to get department head with ID: {}", id);
        Optional<DepartmentHeadEntity> departmentHead = departmentHeadRepository.findById(id);
        return departmentHead.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new department head", description = "Creates a new department head entity and saves it to the database")
    public DepartmentHeadEntity createDepartmentHead(@RequestBody DepartmentHeadEntity departmentHead) {
        logger.info("Request received to create a new department head");
        return departmentHeadRepository.save(departmentHead);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing department head", description = "Updates an existing department head entity with the provided data")
    public ResponseEntity<DepartmentHeadEntity> updateDepartmentHead(@PathVariable("id") Integer id, @RequestBody DepartmentHeadEntity departmentHeadDetails) {
        logger.info("Request received to update department head with ID: {}", id);
        Optional<DepartmentHeadEntity> optionalDepartmentHead = departmentHeadRepository.findById(id);
        if (optionalDepartmentHead.isPresent()) {
            DepartmentHeadEntity departmentHead = optionalDepartmentHead.get();
            departmentHead.setEmployee(departmentHeadDetails.getEmployee());
            departmentHead.setDepartment(departmentHeadDetails.getDepartment());
            departmentHead.setHeadRole(departmentHeadDetails.getHeadRole());
            departmentHead.setHeadTerm(departmentHeadDetails.getHeadTerm());
            return ResponseEntity.ok(departmentHeadRepository.save(departmentHead));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department head by ID", description = "Deletes a department head entity by its ID")
    public ResponseEntity<Void> deleteDepartmentHead(@PathVariable("id") Integer id) {
        logger.info("Request received to delete department head with ID: {}", id);
        if (departmentHeadRepository.existsById(id)) {
            departmentHeadRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a department head", description = "Updates specific fields of an existing department head entity")
    public ResponseEntity<DepartmentHeadEntity> patchDepartmentHead(@PathVariable("id") Integer id, @RequestBody DepartmentHeadEntity departmentHeadDetails) {
        logger.info("Request received to patch department head with ID: {}", id);
        Optional<DepartmentHeadEntity> optionalDepartmentHead = departmentHeadRepository.findById(id);
        if (optionalDepartmentHead.isPresent()) {
            DepartmentHeadEntity departmentHead = optionalDepartmentHead.get();
            if (departmentHeadDetails.getEmployee() != null) {
                departmentHead.setEmployee(departmentHeadDetails.getEmployee());
            }
            if (departmentHeadDetails.getDepartment() != null) {
                departmentHead.setDepartment(departmentHeadDetails.getDepartment());
            }
            if (departmentHeadDetails.getHeadRole() != null) {
                departmentHead.setHeadRole(departmentHeadDetails.getHeadRole());
            }
            if (departmentHeadDetails.getHeadTerm() != null) {
                departmentHead.setHeadTerm(departmentHeadDetails.getHeadTerm());
            }
            return ResponseEntity.ok(departmentHeadRepository.save(departmentHead));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
