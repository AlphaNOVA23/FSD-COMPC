package com.FSD.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FSD.Entity.PerformanceReviewEntity;
import com.FSD.Repository.PerformanceReviewRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/api/performance-reviews")
@Description("Controller for managing performance review entities")
public class PerformanceReviewController {

    @Autowired
    private PerformanceReviewRepository performanceReviewRepository;

    private static final Logger logger = LoggerFactory.getLogger(PerformanceReviewController.class);

    @GetMapping
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all performance reviews", description = "Retrieves a list of all performance review entities")
    public List<PerformanceReviewEntity> getAllPerformanceReviews() {
        logger.info("Request received to get all performance reviews");
        return performanceReviewRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get performance review by ID", description = "Retrieves a performance review entity by its ID")
    public ResponseEntity<PerformanceReviewEntity> getPerformanceReviewById(@PathVariable("id") Integer id) {
        logger.info("Request received to get performance review with ID: {}", id);
        Optional<PerformanceReviewEntity> review = performanceReviewRepository.findById(id);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new performance review", description = "Creates a new performance review entity and saves it to the database")
    public PerformanceReviewEntity createPerformanceReview(@RequestBody PerformanceReviewEntity review) {
        logger.info("Request received to create a new performance review");
        if (review != null) {
            return performanceReviewRepository.save(review);
        }
        return null;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Partially update a performance review", description = "Updates specific fields of an existing performance review entity")
    public ResponseEntity<PerformanceReviewEntity> patchPerformanceReview(@PathVariable("id") Integer id, @RequestBody PerformanceReviewEntity details) {
        logger.info("Request received to patch performance review with ID: {}", id);
        Optional<PerformanceReviewEntity> optionalReview = performanceReviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            PerformanceReviewEntity review = optionalReview.get();
            if (details.getEmployee() != null) {
                review.setEmployee(details.getEmployee());
            }
            if (details.getReviewDate() != null) {
                review.setReviewDate(details.getReviewDate());
            }
            if (details.getPreviousReview() != null) {
                review.setPreviousReview(details.getPreviousReview());
            }
            if (details.getAttendanceId() != null) {
                review.setAttendanceId(details.getAttendanceId());
            }
            if (details.getScoreChange() != null) {
                review.setScoreChange(details.getScoreChange());
            }
            return ResponseEntity.ok(performanceReviewRepository.save(review));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete performance review by ID", description = "Deletes a performance review entity by its ID")
    public ResponseEntity<Void> deletePerformanceReview(@PathVariable("id") Integer id) {
        logger.info("Request received to delete performance review with ID: {}", id);
        if (performanceReviewRepository.existsById(id)) {
            performanceReviewRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
