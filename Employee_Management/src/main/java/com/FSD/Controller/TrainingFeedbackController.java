package com.FSD.Controller;

import com.FSD.Entity.TrainingFeedbackEntity;
import com.FSD.Repository.TrainingFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/training-feedbacks")
public class TrainingFeedbackController {

    @Autowired
    private TrainingFeedbackRepository repository;

    @GetMapping
    public List<TrainingFeedbackEntity> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingFeedbackEntity> getById(@PathVariable Integer id) {
        Optional<TrainingFeedbackEntity> feedback = repository.findById(id);
        return feedback.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/program/{trainingId}")
    public List<TrainingFeedbackEntity> getByTrainingId(@PathVariable Integer trainingId) {
        return repository.findByTrainingProgram_TrainingId(trainingId);
    }

    @PostMapping
    public TrainingFeedbackEntity create(@RequestBody TrainingFeedbackEntity newFeedback) {
        return repository.save(newFeedback);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingFeedbackEntity> update(@PathVariable Integer id, @RequestBody TrainingFeedbackEntity details) {
        Optional<TrainingFeedbackEntity> optional = repository.findById(id);
        if (optional.isPresent()) {
            TrainingFeedbackEntity feedback = optional.get();
            feedback.setFeedbackText(details.getFeedbackText());
            feedback.setRating(details.getRating());
            feedback.setSuggestion(details.getSuggestion());
            return ResponseEntity.ok(repository.save(feedback));
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
