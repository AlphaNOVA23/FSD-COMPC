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

import com.FSD.Entity.PerformanceFeedbackEntity;
import com.FSD.Repository.PerformanceFeedbackRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/performance-feedback")
@Tag(name = "Performance Feedback", description = "APIs for managing employee performance reviews")
public class PerformanceFeedbackController {

    @Autowired
    private PerformanceFeedbackRepository repo;

    private static final Logger logger = LoggerFactory.getLogger(PerformanceFeedbackController.class);

    @GetMapping
    @Operation(summary = "Get all feedback")
    public List<PerformanceFeedbackEntity> getAllFeedback() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feedback by ID")
    public ResponseEntity<PerformanceFeedbackEntity> getFeedbackById(@PathVariable Integer id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get feedback for a specific employee")
    public List<PerformanceFeedbackEntity> getFeedbackForEmployee(@PathVariable Integer employeeId) {
        return repo.findByEmployee_EmployeeId(employeeId);
    }

    @PostMapping
    @Operation(summary = "Create new feedback")
    public PerformanceFeedbackEntity createFeedback(@RequestBody PerformanceFeedbackEntity feedback) {
        // Validation logic can go here (e.g. check if employee and reviewer exist)
        return repo.save(feedback);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update feedback")
    public ResponseEntity<PerformanceFeedbackEntity> patchFeedback(@PathVariable Integer id, @RequestBody PerformanceFeedbackEntity details) {
        Optional<PerformanceFeedbackEntity> opt = repo.findById(id);
        if (opt.isPresent()) {
            PerformanceFeedbackEntity ent = opt.get();
            
            if (details.getRating() != null) ent.setRating(details.getRating());
            if (details.getComments() != null) ent.setComments(details.getComments());
            if (details.getFeedbackDate() != null) ent.setFeedbackDate(details.getFeedbackDate());
            // Usually, we don't change the Employee/Reviewer in a patch, but you could if needed.
            
            return ResponseEntity.ok(repo.save(ent));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void deleteFeedback(@PathVariable Integer id) {
        repo.deleteById(id);
    }
}