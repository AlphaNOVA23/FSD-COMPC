package com.FSD.Controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FSD.Entity.TaskEntity;
import com.FSD.Repository.TaskRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/api/tasks")
@Description("Controller for managing tasks")
public class TaskController {

    @Autowired
    private TaskRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @GetMapping
    public List<TaskEntity> getAllTasks() { return repo.findAll(); }

    @PostMapping
    public TaskEntity createTask(@RequestBody TaskEntity task) { return repo.save(task); }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update task")
    public ResponseEntity<TaskEntity> patchTask(@PathVariable Integer id, @RequestBody TaskEntity details) {
        Optional<TaskEntity> optionalTask = repo.findById(id);
        if (optionalTask.isPresent()) {
            TaskEntity task = optionalTask.get();
            if (details.getTaskName() != null) task.setTaskName(details.getTaskName());
            if (details.getStatus() != null) task.setStatus(details.getStatus());
            if (details.getPriority() != null) task.setPriority(details.getPriority());
            if (details.getAssignedTo() != null) task.setAssignedTo(details.getAssignedTo());
            return ResponseEntity.ok(repo.save(task));
        }
        return ResponseEntity.notFound().build();
    }
}