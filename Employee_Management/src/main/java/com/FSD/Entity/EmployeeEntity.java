package com.FSD.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "employee")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    // --- 1. Link to Position Details (Job Title, Grade) ---
    // mappedBy refers to the 'employee' field in PositionDetailsEntity
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private PositionDetailsEntity positionDetails;

    // --- 2. Link to Salary (Financial Data) ---
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private SalaryEntity salary;

    // --- 3. Link to Security/Credentials (Login Data) ---
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private CredentialEntity credential;

    // --- 4. Link to Department Head Role (If applicable) ---
    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
    private DepartmentHeadEntity departmentHeadRole;

    // --- 5. Link to Responsibilities/Tasks (Project Work) ---
    // Note: We use a List here for one-to-many
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("employee") // Prevent infinite recursion in JSON
    private List<ResponsibilityEntity> responsibilities;

    public EmployeeEntity() {
    }

    public EmployeeEntity(String employeeName) {
        this.employeeName = employeeName;
    }

    // --- Helper Methods to Manage Relationships ---
    
    public void setPositionDetails(PositionDetailsEntity positionDetails) {
        this.positionDetails = positionDetails;
        if (positionDetails != null) {
            positionDetails.setEmployee(this);
        }
    }

    public void setSalary(SalaryEntity salary) {
        this.salary = salary;
        if (salary != null) {
            salary.setEmployee(this);
        }
    }

    public void setCredential(CredentialEntity credential) {
        this.credential = credential;
        if (credential != null) {
            credential.setEmployee(this);
        }
    }

    // --- Getters and Setters ---

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public PositionDetailsEntity getPositionDetails() { return positionDetails; }

    public SalaryEntity getSalary() { return salary; }

    public CredentialEntity getCredential() { return credential; }

    public DepartmentHeadEntity getDepartmentHeadRole() { return departmentHeadRole; }
    public void setDepartmentHeadRole(DepartmentHeadEntity departmentHeadRole) { this.departmentHeadRole = departmentHeadRole; }

    public List<ResponsibilityEntity> getResponsibilities() { return responsibilities; }
    public void setResponsibilities(List<ResponsibilityEntity> responsibilities) { this.responsibilities = responsibilities; }
}