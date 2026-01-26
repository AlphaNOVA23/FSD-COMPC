package com.FSD.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "organization")
    private String organization;

    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;

    @Column(name = "client_type")
    private String clientType; // e.g., "Corporate", "Government"

    @Column(name = "status")
    private String status; // e.g., "Active", "Inactive"

    // One Client has many Projects
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("client") // Prevent infinite recursion
    private Set<ProjectEntity> projects = new HashSet<>();

    public ClientEntity() {}

    // Getters and Setters
    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }

    public String getClientType() { return clientType; }
    public void setClientType(String clientType) { this.clientType = clientType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Set<ProjectEntity> getProjects() { return projects; }
    public void setProjects(Set<ProjectEntity> projects) { this.projects = projects; }
}