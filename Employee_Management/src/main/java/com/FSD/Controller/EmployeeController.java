package com.FSD.Controller;

import java.util.List;
import java.util.Optional;
import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.FSD.Entity.CredentialEntity;
import com.FSD.Entity.EmployeeEntity;
import com.FSD.Entity.PositionDetailsEntity;
import com.FSD.Entity.ResponsibilityEntity;
import com.FSD.Entity.SalaryEntity;
import com.FSD.Entity.LoginDetailsEntity;
import com.FSD.Repository.EmployeeRepository;
import com.FSD.Repository.LoginDetailsRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Management", description = "APIs for managing employee entities and their relationships")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LoginDetailsRepository loginDetailsRepository;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    // --- Core CRUD Operations ---

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get current logged-in employee", description = "Retrieves the employee profile of the currently authenticated user based on JWT")
    public ResponseEntity<EmployeeEntity> getCurrentEmployee(Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        
        String username = principal.getName();
        LoginDetailsEntity loginDetails = loginDetailsRepository.findByUsername(username);
        
        if (loginDetails != null && loginDetails.getCredential() != null) {
            EmployeeEntity employee = loginDetails.getCredential().getEmployee();
            return ResponseEntity.ok(employee);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all employees", description = "Retrieves a list of all employee entities")
    public ResponseEntity<List<EmployeeEntity>> getAllEmployees() {
        logger.info("Request received to get all employees");
        List<EmployeeEntity> employees = employeeRepository.findAll();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get employee by ID", description = "Retrieves an employee entity by its ID with all relationships")
    public ResponseEntity<EmployeeEntity> getEmployeeById(@PathVariable("id") Integer id) {
        logger.info("Request received to get employee with ID: {}", id);
        Optional<EmployeeEntity> employee = employeeRepository.findById(id);
        return employee.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Employee not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Custom search endpoint (Mapped to /employees/name/{name})
    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get employees by name", description = "Retrieves employees by name (case-insensitive partial match)")
    public ResponseEntity<List<EmployeeEntity>> getEmployeesByName(@PathVariable("name") String name) {
        logger.info("Request received to get employees with name containing: {}", name);
        // Ensure your Repository has this method or use a custom query
        // If not present, this line might need adjustment to findByEmployeeNameContaining
        List<EmployeeEntity> employees = employeeRepository.findAll().stream()
                .filter(e -> e.getEmployeeName().toLowerCase().contains(name.toLowerCase()))
                .toList(); 
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new employee", description = "Creates a new employee entity with optional related data")
    public ResponseEntity<EmployeeEntity> createEmployee(@RequestBody EmployeeEntity newEmployee) {
        logger.info("Request received to create a new employee: {}", newEmployee.getEmployeeName());
        
        // Ensure bidirectional relationships are set properly
        if (newEmployee.getPositionDetails() != null) {
            newEmployee.getPositionDetails().setEmployee(newEmployee);
        }
        if (newEmployee.getSalary() != null) {
            newEmployee.getSalary().setEmployee(newEmployee);
        }
        if (newEmployee.getCredential() != null) {
            newEmployee.getCredential().setEmployee(newEmployee);
        }
        if (newEmployee.getDepartmentHeadRole() != null) {
            newEmployee.getDepartmentHeadRole().setEmployee(newEmployee);
        }
        if (newEmployee.getResponsibilities() != null) {
            newEmployee.getResponsibilities().forEach(resp -> resp.setEmployee(newEmployee));
        }
        
        EmployeeEntity savedEmployee = employeeRepository.save(newEmployee);
        logger.info("Created employee with ID: {}", savedEmployee.getEmployeeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an employee", description = "Fully updates an existing employee entity")
    public ResponseEntity<EmployeeEntity> updateEmployee(
            @PathVariable("id") Integer id, 
            @Valid @RequestBody EmployeeEntity employeeDetails) {
        logger.info("Request received to update employee with ID: {}", id);
        
        Optional<EmployeeEntity> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            EmployeeEntity employee = optionalEmployee.get();
            employee.setEmployeeName(employeeDetails.getEmployeeName());
            
            // Update position details
            if (employeeDetails.getPositionDetails() != null) {
                employee.setPositionDetails(employeeDetails.getPositionDetails());
            }
            
            // Update salary
            if (employeeDetails.getSalary() != null) {
                employee.setSalary(employeeDetails.getSalary());
            }
            
            // Update credentials
            if (employeeDetails.getCredential() != null) {
                employee.setCredential(employeeDetails.getCredential());
            }
            
            EmployeeEntity updatedEmployee = employeeRepository.save(employee);
            logger.info("Updated employee with ID: {}", id);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            logger.warn("Employee not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Partially update an employee", description = "Updates specific fields of an existing employee entity")
    public ResponseEntity<EmployeeEntity> patchEmployee(
            @PathVariable("id") Integer id, 
            @RequestBody EmployeeEntity employeeDetails) {
        logger.info("Request received to patch employee with ID: {}", id);
        
        Optional<EmployeeEntity> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            EmployeeEntity employee = optionalEmployee.get();
            
            // Update only non-null fields
            if (employeeDetails.getEmployeeName() != null) {
                employee.setEmployeeName(employeeDetails.getEmployeeName());
            }
            if (employeeDetails.getDepartment() != null) {
                employee.setDepartment(employeeDetails.getDepartment());
            }
            if (employeeDetails.getPositionDetails() != null) {
                employee.setPositionDetails(employeeDetails.getPositionDetails());
            }
            if (employeeDetails.getSalary() != null) {
                employee.setSalary(employeeDetails.getSalary());
            }
            if (employeeDetails.getCredential() != null) {
                employee.setCredential(employeeDetails.getCredential());
            }
            if (employeeDetails.getDepartmentHeadRole() != null) {
                employeeDetails.getDepartmentHeadRole().setEmployee(employee);
                employee.setDepartmentHeadRole(employeeDetails.getDepartmentHeadRole());
            }
            
            EmployeeEntity updatedEmployee = employeeRepository.save(employee);
            logger.info("Patched employee with ID: {}", id);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            logger.warn("Employee not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @org.springframework.transaction.annotation.Transactional
    @Operation(summary = "Delete employee by ID", description = "Deletes an employee entity and all cascading relationships")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Integer id) {
        logger.info("Request received to delete employee with ID: {}", id);
        
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            logger.info("Deleted employee with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Employee not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // --- Relationship-Specific Endpoints ---

    @GetMapping("/{id}/position")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get employee position details", description = "Retrieves position details for a specific employee")
    public ResponseEntity<PositionDetailsEntity> getEmployeePosition(@PathVariable("id") Integer id) {
        logger.info("Request received to get position details for employee ID: {}", id);
        
        Optional<EmployeeEntity> employee = employeeRepository.findById(id);
        if (employee.isPresent() && employee.get().getPositionDetails() != null) {
            return ResponseEntity.ok(employee.get().getPositionDetails());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/salary")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get employee salary", description = "Retrieves salary information for a specific employee")
    public ResponseEntity<SalaryEntity> getEmployeeSalary(@PathVariable("id") Integer id) {
        logger.info("Request received to get salary for employee ID: {}", id);
        
        Optional<EmployeeEntity> employee = employeeRepository.findById(id);
        if (employee.isPresent() && employee.get().getSalary() != null) {
            return ResponseEntity.ok(employee.get().getSalary());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/credentials")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get employee credentials", description = "Retrieves credentials for a specific employee")
    public ResponseEntity<CredentialEntity> getEmployeeCredentials(@PathVariable("id") Integer id) {
        logger.info("Request received to get credentials for employee ID: {}", id);
        
        Optional<EmployeeEntity> employee = employeeRepository.findById(id);
        if (employee.isPresent() && employee.get().getCredential() != null) {
            return ResponseEntity.ok(employee.get().getCredential());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/responsibilities")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get employee responsibilities", description = "Retrieves all responsibilities for a specific employee")
    public ResponseEntity<List<ResponsibilityEntity>> getEmployeeResponsibilities(@PathVariable("id") Integer id) {
        logger.info("Request received to get responsibilities for employee ID: {}", id);
        
        Optional<EmployeeEntity> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get().getResponsibilities());
        }
        return ResponseEntity.notFound().build();
    }
}