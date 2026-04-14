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

import com.FSD.Entity.ClientEntity;
import com.FSD.Repository.ClientRepository;

import io.swagger.v3.oas.annotations.Operation;
import jdk.jfr.Description;

@RestController
@RequestMapping("/api/clients")
@Description("Controller for managing clients")
public class ClientController {

    @Autowired
    private ClientRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @GetMapping
    public List<ClientEntity> getAll() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getById(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ClientEntity create(@RequestBody ClientEntity client) { return repo.save(client); }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update client")
    public ResponseEntity<ClientEntity> patch(@PathVariable Integer id, @RequestBody ClientEntity details) {
        Optional<ClientEntity> opt = repo.findById(id);
        if (opt.isPresent()) {
            ClientEntity c = opt.get();
            if (details.getClientName() != null) c.setClientName(details.getClientName());
            if (details.getOrganization() != null) c.setOrganization(details.getOrganization());
            if (details.getBudget() != null) c.setBudget(details.getBudget());
            if (details.getStatus() != null) c.setStatus(details.getStatus());
            return ResponseEntity.ok(repo.save(c));
        }
        return ResponseEntity.notFound().build();
    }
}