package com.FSD.Controller;

import com.FSD.Entity.EmployeeTrainingEntity;
import com.FSD.Repository.EmployeeTrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee-trainings")
public class EmployeeTrainingController {

    @Autowired
    private EmployeeTrainingRepository repository;

    @GetMapping
    public List<EmployeeTrainingEntity> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeTrainingEntity> getById(@PathVariable Integer id) {
        Optional<EmployeeTrainingEntity> enrollment = repository.findById(id);
        return enrollment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public List<EmployeeTrainingEntity> getByEmployeeId(@PathVariable Integer employeeId) {
        return repository.findByEmployee_EmployeeId(employeeId);
    }

    @PostMapping
    public EmployeeTrainingEntity create(@RequestBody EmployeeTrainingEntity newEnrollment) {
        return repository.save(newEnrollment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeTrainingEntity> update(@PathVariable Integer id, @RequestBody EmployeeTrainingEntity details) {
        Optional<EmployeeTrainingEntity> optional = repository.findById(id);
        if (optional.isPresent()) {
            EmployeeTrainingEntity enrollment = optional.get();
            enrollment.setGrade(details.getGrade());
            enrollment.setStatus(details.getStatus());
            // Intentionally bypassing re-linking of FKs here for safety, they should be created properly on POST
            return ResponseEntity.ok(repository.save(enrollment));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
