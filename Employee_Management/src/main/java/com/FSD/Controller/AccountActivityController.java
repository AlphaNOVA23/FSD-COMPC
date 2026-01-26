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

import com.FSD.Entity.AccountActivityEntity;
import com.FSD.Repository.AccountActivityRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/account-activities")
@Description("Controller for tracking user account activity logs")
public class AccountActivityController {

    @Autowired
    private AccountActivityRepository repo;
    
    private static final Logger logger = LoggerFactory.getLogger(AccountActivityController.class);

    @GetMapping
    @Operation(summary = "Get all activities", description = "Retrieves a list of all account activity logs")
    public List<AccountActivityEntity> getAllActivities() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get activity by ID")
    public ResponseEntity<AccountActivityEntity> getActivityById(@PathVariable Integer id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get activities by User ID", description = "Retrieves all activity logs for a specific credential user")
    public List<AccountActivityEntity> getActivitiesByUserId(@PathVariable Integer userId) {
        return repo.findByCredential_UserId(userId);
    }

    @PostMapping
    @Operation(summary = "Log new activity")
    public AccountActivityEntity createActivity(@RequestBody AccountActivityEntity activity) {
        // createdAt is handled by @PrePersist in the Entity
        return repo.save(activity);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update activity log")
    public ResponseEntity<AccountActivityEntity> patchActivity(@PathVariable Integer id, @RequestBody AccountActivityEntity details) {
        Optional<AccountActivityEntity> opt = repo.findById(id);
        if (opt.isPresent()) {
            AccountActivityEntity entity = opt.get();
            
            if (details.getCredential() != null) {
                entity.setCredential(details.getCredential());
            }
            if (details.getStatus() != null) {
                entity.setStatus(details.getStatus());
            }
            if (details.getLastLogin() != null) {
                entity.setLastLogin(details.getLastLogin());
            }
            
            return ResponseEntity.ok(repo.save(entity));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete activity log")
    public void deleteActivity(@PathVariable Integer id) {
        repo.deleteById(id);
    }
}