package com.FSD.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FSD.Entity.CredentialEntity;
import com.FSD.Repository.CredentialRepository;

import jdk.jfr.Description;

@RestController
@RequestMapping("/api/credentials")
@Description("Controller for managing user credentials")
public class CredentialController {

    @Autowired
    private CredentialRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    @GetMapping
    public List<CredentialEntity> getAll() { return repo.findAll(); }

    @PostMapping
    public CredentialEntity create(@RequestBody CredentialEntity entity) {
        return repo.save(entity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        repo.deleteById(id);
    }
}