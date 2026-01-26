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

import com.FSD.Entity.PerformanceEvaluationEntity;
import com.FSD.Repository.PerformanceEvaluationRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/performance_evaluations")
@Description("Controller for managing performance evaluation entities")
public class PerformanceEvaluationController {

    @Autowired
    private PerformanceEvaluationRepository evaluationRepository;

    private static final Logger logger = LoggerFactory.getLogger(PerformanceEvaluationController.class);

    @GetMapping
    @Transactional
    @Operation(summary = "Get all evaluations", description = "Retrieves a list of all performance evaluation entities")
    public List<PerformanceEvaluationEntity> getAllEvaluations() {
        logger.info("Request received to get all performance evaluations");
        return evaluationRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get evaluation by ID", description = "Retrieves a performance evaluation entity by its ID")
    public ResponseEntity<PerformanceEvaluationEntity> getEvaluationById(@PathVariable("id") Integer id) {
        logger.info("Request received to get evaluation with ID: {}", id);
        Optional<PerformanceEvaluationEntity> evaluation = evaluationRepository.findById(id);
        return evaluation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new evaluation", description = "Creates a new performance evaluation entity and saves it to the database")
    public PerformanceEvaluationEntity createEvaluation(@RequestBody PerformanceEvaluationEntity evaluation) {
        logger.info("Request received to create a new evaluation for employee ID: {}", evaluation.getEmployeeId());
        return evaluationRepository.save(evaluation);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing evaluation", description = "Updates an existing evaluation entity with the provided data")
    public ResponseEntity<PerformanceEvaluationEntity> updateEvaluation(@PathVariable("id") Integer id, @RequestBody PerformanceEvaluationEntity evaluationDetails) {
        logger.info("Request received to update evaluation with ID: {}", id);
        Optional<PerformanceEvaluationEntity> optionalEvaluation = evaluationRepository.findById(id);
        if (optionalEvaluation.isPresent()) {
            PerformanceEvaluationEntity evaluation = optionalEvaluation.get();
            evaluation.setEmployeeId(evaluationDetails.getEmployeeId());
            evaluation.setEvaluationPeriod(evaluationDetails.getEvaluationPeriod());
            evaluation.setPerformanceRating(evaluationDetails.getPerformanceRating());
            evaluation.setComments(evaluationDetails.getComments());
            return ResponseEntity.ok(evaluationRepository.save(evaluation));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete evaluation by ID", description = "Deletes a performance evaluation entity by its ID")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable("id") Integer id) {
        logger.info("Request received to delete evaluation with ID: {}", id);
        if (evaluationRepository.existsById(id)) {
            evaluationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update an evaluation", description = "Updates specific fields of an existing performance evaluation entity")
    public ResponseEntity<PerformanceEvaluationEntity> patchEvaluation(@PathVariable("id") Integer id, @RequestBody PerformanceEvaluationEntity evaluationDetails) {
        logger.info("Request received to patch evaluation with ID: {}", id);
        Optional<PerformanceEvaluationEntity> optionalEvaluation = evaluationRepository.findById(id);
        if (optionalEvaluation.isPresent()) {
            PerformanceEvaluationEntity evaluation = optionalEvaluation.get();
            if (evaluationDetails.getEmployeeId() != null) {
                evaluation.setEmployeeId(evaluationDetails.getEmployeeId());
            }
            if (evaluationDetails.getEvaluationPeriod() != null) {
                evaluation.setEvaluationPeriod(evaluationDetails.getEvaluationPeriod());
            }
            if (evaluationDetails.getPerformanceRating() != null) {
                evaluation.setPerformanceRating(evaluationDetails.getPerformanceRating());
            }
            if (evaluationDetails.getComments() != null) {
                evaluation.setComments(evaluationDetails.getComments());
            }
            return ResponseEntity.ok(evaluationRepository.save(evaluation));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}