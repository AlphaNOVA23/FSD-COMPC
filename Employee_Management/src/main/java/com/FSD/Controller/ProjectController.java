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

import com.FSD.Entity.ProjectEntity;
import com.FSD.Repository.ProjectRepository;

import jdk.jfr.Description;

@RestController
@RequestMapping("/projects")
@Description("Controller for managing projects")
public class ProjectController {

    @Autowired
    private ProjectRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @GetMapping
    public List<ProjectEntity> getAll() { return repo.findAll(); }

    @PostMapping
    public ProjectEntity create(@RequestBody ProjectEntity project) { return repo.save(project); }

    @GetMapping("/by-client/{clientId}")
    public List<ProjectEntity> getByClient(@PathVariable Integer clientId) {
        return repo.findByClient_ClientId(clientId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectEntity> patch(@PathVariable Integer id, @RequestBody ProjectEntity details) {
        Optional<ProjectEntity> opt = repo.findById(id);
        if (opt.isPresent()) {
            ProjectEntity p = opt.get();
            if (details.getProjectName() != null) p.setProjectName(details.getProjectName());
            if (details.getDepartment() != null) p.setDepartment(details.getDepartment());
            if (details.getProjectLead() != null) p.setProjectLead(details.getProjectLead());
            if (details.getClient() != null) p.setClient(details.getClient());
            return ResponseEntity.ok(repo.save(p));
        }
        return ResponseEntity.notFound().build();
    }
}