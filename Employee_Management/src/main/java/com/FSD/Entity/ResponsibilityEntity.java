package com.FSD.Entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "responsibility")
public class ResponsibilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "responsibility_id")
    private Integer responsibilityId;

    // ✅ REFACTORED: Now links to the actual ProjectEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @Enumerated(EnumType.STRING)
    @Column(name = "responsibility_type", nullable = false)
    private ResponsibilityType responsibilityType;

    @Enumerated(EnumType.STRING)
    @Column(name = "clearance_level", nullable = false)
    private ClearanceLevel clearanceLevel;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private EmployeeEntity createdBy;

    @Column(name = "created_date")
    private OffsetDateTime createdDate;

    public enum ResponsibilityType { PROJECT_LEAD, CONTRIBUTOR, REVIEWER, MENTOR, STAKEHOLDER }
    public enum ClearanceLevel { NONE, INTERNAL, CONFIDENTIAL, SECRET, TOP_SECRET }

    public ResponsibilityEntity() {}

    // Getters and Setters
    public Integer getResponsibilityId() { return responsibilityId; }
    public void setResponsibilityId(Integer responsibilityId) { this.responsibilityId = responsibilityId; }

    public ProjectEntity getProject() { return project; }
    public void setProject(ProjectEntity project) { this.project = project; }

    public ResponsibilityType getResponsibilityType() { return responsibilityType; }
    public void setResponsibilityType(ResponsibilityType responsibilityType) { this.responsibilityType = responsibilityType; }

    public ClearanceLevel getClearanceLevel() { return clearanceLevel; }
    public void setClearanceLevel(ClearanceLevel clearanceLevel) { this.clearanceLevel = clearanceLevel; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public EmployeeEntity getEmployee() { return employee; }
    public void setEmployee(EmployeeEntity employee) { this.employee = employee; }

    public EmployeeEntity getCreatedBy() { return createdBy; }
    public void setCreatedBy(EmployeeEntity createdBy) { this.createdBy = createdBy; }

    public OffsetDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(OffsetDateTime createdDate) { this.createdDate = createdDate; }
}