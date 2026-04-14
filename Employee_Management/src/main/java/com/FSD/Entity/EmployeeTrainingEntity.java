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
@Table(name = "employee_training")
public class EmployeeTrainingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_training_id")
    private Integer employeeTrainingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employeeTrainings"})
    private TrainingProgramEntity trainingProgram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"credential", "salary", "positionDetails", "responsibilities", "trainings", "department"})
    private EmployeeEntity employee;

    @Column(name = "grade", length = 50)
    private String grade;

    @Column(name = "status", length = 50)
    private String status;

    public EmployeeTrainingEntity() {}

    public EmployeeTrainingEntity(TrainingProgramEntity trainingProgram, EmployeeEntity employee, String grade, String status) {
        this.trainingProgram = trainingProgram;
        this.employee = employee;
        this.grade = grade;
        this.status = status;
    }

    public Integer getEmployeeTrainingId() {
        return employeeTrainingId;
    }

    public void setEmployeeTrainingId(Integer employeeTrainingId) {
        this.employeeTrainingId = employeeTrainingId;
    }

    public TrainingProgramEntity getTrainingProgram() {
        return trainingProgram;
    }

    public void setTrainingProgram(TrainingProgramEntity trainingProgram) {
        this.trainingProgram = trainingProgram;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
