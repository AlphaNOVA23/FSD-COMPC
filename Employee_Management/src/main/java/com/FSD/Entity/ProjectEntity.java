package com.FSD.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "project_duration")
    private String projectDuration; // e.g., "6 Months"

    // Linked to Client
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties("projects")
    private ClientEntity client;

    // Standardized: Linked to existing DepartmentEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties({"employees", "departmentHead", "projects"})
    private DepartmentEntity department;

    // Standardized: Linked to existing EmployeeEntity (Project Lead)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id") 
    @JsonIgnoreProperties({"responsibilities", "department", "trainings"})
    private EmployeeEntity projectLead;

    public ProjectEntity() {}

    // Getters and Setters
    public Integer getProjectId() { return projectId; }
    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getProjectDuration() { return projectDuration; }
    public void setProjectDuration(String projectDuration) { this.projectDuration = projectDuration; }

    public ClientEntity getClient() { return client; }
    public void setClient(ClientEntity client) { this.client = client; }

    public DepartmentEntity getDepartment() { return department; }
    public void setDepartment(DepartmentEntity department) { this.department = department; }

    public EmployeeEntity getProjectLead() { return projectLead; }
    public void setProjectLead(EmployeeEntity projectLead) { this.projectLead = projectLead; }
}