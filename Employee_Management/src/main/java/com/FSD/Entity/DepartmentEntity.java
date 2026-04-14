package com.FSD.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Represents a department within the organization.
 * This entity is mapped to the "department" table in the database.
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "department")
public class DepartmentEntity {

    /**
     * The unique identifier for the department.
     * This is the primary key and is auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Integer departmentId;

    /**
     * The name of the department.
     * This field cannot be null.
     */
    @Column(name = "department_name", nullable = false, length = 100)
    private String departmentName;

    /**
     * The physical location of the department.
     */
    @Column(name = "department_location", length = 255)
    private String departmentLocation;

    /**
     * The head of the department.
     * This is the "parent" side of the relationship.
     * @JsonManagedReference handles the circular dependency during serialization.
     * FetchType.LAZY is added to prevent infinite loops when loading entities.
     */
    @OneToOne(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("department") // Stops infinite recursion but still shows the head details
    private DepartmentHeadEntity departmentHead;

    /**
     * The maximum number of employees the department can have.
     */
    @Column(name = "department_capacity")
    private Integer departmentCapacity;

    /**
     * The contact information for the department.
     */
    @Column(name = "department_contact", length = 255)
    private String departmentContact;

    /**
     * The standard employees assigned to this department.
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"department", "hibernateLazyInitializer", "handler"})
    private java.util.List<EmployeeEntity> employees;

    // Default constructor
    public DepartmentEntity() {
    }

    // Parameterized constructor
public DepartmentEntity(String departmentName, String departmentLocation, DepartmentHeadEntity departmentHead, Integer departmentCapacity, String departmentContact) {
    this.departmentName = departmentName;
    this.departmentLocation = departmentLocation;
    this.departmentHead = departmentHead;
    this.departmentCapacity = departmentCapacity;
    this.departmentContact = departmentContact;
}

    // Getters and Setters

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentLocation() {
        return departmentLocation;
    }

    public void setDepartmentLocation(String departmentLocation) {
        this.departmentLocation = departmentLocation;
    }

    public DepartmentHeadEntity getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(DepartmentHeadEntity departmentHead) {
        this.departmentHead = departmentHead;
    }

    public Integer getDepartmentCapacity() {
        return departmentCapacity;
    }

    public void setDepartmentCapacity(Integer departmentCapacity) {
        this.departmentCapacity = departmentCapacity;
    }

    public String getDepartmentContact() {
        return departmentContact;
    }

    public void setDepartmentContact(String departmentContact) {
        this.departmentContact = departmentContact;
    }

    public java.util.List<EmployeeEntity> getEmployees() {
        return employees;
    }

    public void setEmployees(java.util.List<EmployeeEntity> employees) {
        this.employees = employees;
    }
}
