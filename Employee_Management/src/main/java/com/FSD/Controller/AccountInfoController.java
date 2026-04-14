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

import com.FSD.Entity.AccountInfoEntity;
import com.FSD.Repository.AccountInfoRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/api/account-info")
@Description("Controller for managing account details")
public class AccountInfoController {

    @Autowired
    private AccountInfoRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(AccountInfoController.class);

    @GetMapping
    public List<AccountInfoEntity> getAll() { return repo.findAll(); }

    @PostMapping
    public AccountInfoEntity create(@RequestBody AccountInfoEntity entity) { return repo.save(entity); }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update account info")
    public ResponseEntity<AccountInfoEntity> patch(@PathVariable Integer id, @RequestBody AccountInfoEntity details) {
        Optional<AccountInfoEntity> opt = repo.findById(id);
        if (opt.isPresent()) {
            AccountInfoEntity ent = opt.get();
            if (details.getEmail() != null) ent.setEmail(details.getEmail());
            if (details.getRole() != null) ent.setRole(details.getRole());
            return ResponseEntity.ok(repo.save(ent));
        }
        return ResponseEntity.notFound().build();
    }
}