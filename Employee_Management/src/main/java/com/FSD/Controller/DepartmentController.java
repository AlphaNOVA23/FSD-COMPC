package com.FSD.Controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FSD.Entity.DepartmentEntity;
import com.FSD.Repository.DepartmentRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/departments")
@Description("Controller for managing department entities")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @GetMapping
    @Transactional
    @Operation(summary = "Get all departments", description = "Retrieves a list of all department entities")
    public List<DepartmentEntity> getAllDepartments() {
        logger.info("Request received to get all departments");
        return departmentRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID", description = "Retrieves a department entity by its ID")
    public ResponseEntity<DepartmentEntity> getDepartmentById(@PathVariable("id") Integer id) {
        logger.info("Request received to get department with ID: {}", id);
        Optional<DepartmentEntity> department = departmentRepository.findById(id);
        return department.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new department", description = "Creates a new department entity and saves it to the database")
    public DepartmentEntity createDepartment(@RequestBody DepartmentEntity department) {
        logger.info("Request received to create a new department");
        return departmentRepository.save(department);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing department", description = "Updates an existing department entity with the provided data")
    public ResponseEntity<DepartmentEntity> updateDepartment(@PathVariable("id") Integer id, @RequestBody DepartmentEntity departmentDetails) {
        logger.info("Request received to update department with ID: {}", id);
        Optional<DepartmentEntity> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            DepartmentEntity department = optionalDepartment.get();
            department.setDepartmentName(departmentDetails.getDepartmentName());
            department.setDepartmentLocation(departmentDetails.getDepartmentLocation());
            department.setDepartmentHead(departmentDetails.getDepartmentHead());
            department.setDepartmentCapacity(departmentDetails.getDepartmentCapacity());
            department.setDepartmentContact(departmentDetails.getDepartmentContact());
            return ResponseEntity.ok(departmentRepository.save(department));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department by ID", description = "Deletes a department entity by its ID")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("id") Integer id) {
        logger.info("Request received to delete department with ID: {}", id);
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a department", description = "Updates specific fields of an existing department entity")
    public ResponseEntity<DepartmentEntity> patchDepartment(@PathVariable("id") Integer id, @RequestBody DepartmentEntity departmentDetails) {
        logger.info("Request received to patch department with ID: {}", id);
        Optional<DepartmentEntity> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()) {
            DepartmentEntity department = optionalDepartment.get();
            if (departmentDetails.getDepartmentName() != null) {
                department.setDepartmentName(departmentDetails.getDepartmentName());
            }
            if (departmentDetails.getDepartmentLocation() != null) {
                department.setDepartmentLocation(departmentDetails.getDepartmentLocation());
            }
            if (departmentDetails.getDepartmentHead() != null) {
                department.setDepartmentHead(departmentDetails.getDepartmentHead());
            }
            if (departmentDetails.getDepartmentCapacity() != null) {
                department.setDepartmentCapacity(departmentDetails.getDepartmentCapacity());
            }
            if (departmentDetails.getDepartmentContact() != null) {
                department.setDepartmentContact(departmentDetails.getDepartmentContact());
            }
            return ResponseEntity.ok(departmentRepository.save(department));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
