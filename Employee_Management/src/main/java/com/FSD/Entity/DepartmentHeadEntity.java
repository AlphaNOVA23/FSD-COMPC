package com.FSD.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Represents the head of a department.
 * This entity is mapped to the "department_head" table in the database.
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "department_head")
public class DepartmentHeadEntity {

    /**
     * The unique identifier for the department head.
     * This is the primary key and is auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "head_id")
    private Integer headId;

    /**
     * The employee who is the department head.
     * This establishes a one-to-one relationship with the EmployeeEntity.
     * FetchType.LAZY is added to prevent infinite loops when loading entities.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    @JsonIgnoreProperties({"departmentHeadRole", "responsibilities", "credential", "salary", "positionDetails"})
    private EmployeeEntity employee;
    
    /**
     * The department this person is the head of.
     * This is the "child" side of the relationship.
     * @JsonBackReference prevents the infinite loop during serialization.
     * FetchType.LAZY is added to prevent infinite loops when loading entities.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    @JsonBackReference
    private DepartmentEntity department;

    /**
     * The role of the department head (e.g., "Manager", "Director").
     */
    @Column(name = "head_role", length = 100)
    private String headRole;

    /**
     * The term of the department head (e.g., "2023-2025").
     */
    @Column(name = "head_term", length = 50)
    private String headTerm;

    // Default constructor
    public DepartmentHeadEntity() {
    }

    // Parameterized constructor
    public DepartmentHeadEntity(EmployeeEntity employee, DepartmentEntity department, String headRole, String headTerm) {
        this.employee = employee;
        this.department = department;
        this.headRole = headRole;
        this.headTerm = headTerm;
    }

    // Getters and Setters

    public Integer getHeadId() {
        return headId;
    }

    public void setHeadId(Integer headId) {
        this.headId = headId;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }
    
    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

    public String getHeadRole() {
        return headRole;
    }

    public void setHeadRole(String headRole) {
        this.headRole = headRole;
    }

    public String getHeadTerm() {
        return headTerm;
    }

    public void setHeadTerm(String headTerm) {
        this.headTerm = headTerm;
    }
}
