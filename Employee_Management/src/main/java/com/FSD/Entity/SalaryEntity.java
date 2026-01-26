package com.FSD.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "salaries")
public class SalaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Integer salaryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id", nullable = false)
    @JsonIgnore
    private EmployeeEntity employee;

    @Column(name = "base_salary")
    private Double baseSalary;

    @Column(name = "bonus")
    private Double bonus;

    @Column(name = "deductions")
    private Double deductions;

    @Column(name = "net_salary")
    private Double netSalary;

    public SalaryEntity() {
    }

    public SalaryEntity(EmployeeEntity employee, Double baseSalary, Double bonus, Double deductions) {
        this.employee = employee;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        calculateNetSalary();
    }

    @PrePersist
    @PreUpdate
    public void calculateNetSalary() {
        // Safe null checks
        double base = (baseSalary != null) ? baseSalary : 0.0;
        double bns = (bonus != null) ? bonus : 0.0;
        double ded = (deductions != null) ? deductions : 0.0;
        this.netSalary = base + bns - ded;
    }

    // Getters and Setters
    public Integer getSalaryId() { return salaryId; }
    public void setSalaryId(Integer salaryId) { this.salaryId = salaryId; }

    public EmployeeEntity getEmployee() { return employee; }
    public void setEmployee(EmployeeEntity employee) { this.employee = employee; }

    public Double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(Double baseSalary) { this.baseSalary = baseSalary; }

    public Double getBonus() { return bonus; }
    public void setBonus(Double bonus) { this.bonus = bonus; }

    public Double getDeductions() { return deductions; }
    public void setDeductions(Double deductions) { this.deductions = deductions; }

    public Double getNetSalary() { return netSalary; }
    public void setNetSalary(Double netSalary) { this.netSalary = netSalary; }

}