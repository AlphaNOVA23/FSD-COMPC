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

import com.FSD.Entity.LoginDetailsEntity;
import com.FSD.Repository.LoginDetailsRepository;

import jdk.jfr.Description;

@RestController
@RequestMapping("/login-details")
@Description("Controller for managing login credentials")
public class LoginDetailsController {

    @Autowired
    private LoginDetailsRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(LoginDetailsController.class);

    @GetMapping
    public List<LoginDetailsEntity> getAll() { return repo.findAll(); }

    @PostMapping
    public LoginDetailsEntity create(@RequestBody LoginDetailsEntity entity) { return repo.save(entity); }

    @PatchMapping("/{id}")
    public ResponseEntity<LoginDetailsEntity> patch(@PathVariable Integer id, @RequestBody LoginDetailsEntity details) {
        Optional<LoginDetailsEntity> opt = repo.findById(id);
        if (opt.isPresent()) {
            LoginDetailsEntity ent = opt.get();
            if (details.getUsername() != null) ent.setUsername(details.getUsername());
            if (details.getPasswordHash() != null) ent.setPasswordHash(details.getPasswordHash());
            return ResponseEntity.ok(repo.save(ent));
        }
        return ResponseEntity.notFound().build();
    }
}