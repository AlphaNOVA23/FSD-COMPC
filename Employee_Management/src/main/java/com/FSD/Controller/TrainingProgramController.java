package com.FSD.Controller;

import com.FSD.Entity.TrainingProgramEntity;
import com.FSD.Repository.TrainingProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/training-programs")
public class TrainingProgramController {

    @Autowired
    private TrainingProgramRepository repository;

    @GetMapping
    public List<TrainingProgramEntity> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingProgramEntity> getById(@PathVariable Integer id) {
        Optional<TrainingProgramEntity> program = repository.findById(id);
        return program.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TrainingProgramEntity create(@RequestBody TrainingProgramEntity newProgram) {
        return repository.save(newProgram);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingProgramEntity> update(@PathVariable Integer id, @RequestBody TrainingProgramEntity details) {
        Optional<TrainingProgramEntity> optionalProgram = repository.findById(id);
        if (optionalProgram.isPresent()) {
            TrainingProgramEntity program = optionalProgram.get();
            program.setProgramName(details.getProgramName());
            program.setTrainer(details.getTrainer());
            program.setDate(details.getDate());
            program.setDurationHours(details.getDurationHours());
            return ResponseEntity.ok(repository.save(program));
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
