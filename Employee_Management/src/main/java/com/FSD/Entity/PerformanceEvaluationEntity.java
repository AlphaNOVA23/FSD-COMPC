package com.FSD.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents a formal performance evaluation record.
 * This entity is mapped to the "performance_evaluation" table in the database.
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "performance_evaluation")
public class PerformanceEvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluation_id")
    private Integer evaluationId;

    /**
     * The ID of the employee being evaluated.
     * Mapped as an Integer to ensure compatibility with the SQL script fk constraints.
     */
    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    @Column(name = "evaluation_period", nullable = false, length = 100)
    private String evaluationPeriod;

    @Column(name = "performance_rating", length = 50)
    private String performanceRating;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    // Default constructor
    public PerformanceEvaluationEntity() {
    }

    // Parameterized constructor
    public PerformanceEvaluationEntity(Integer employeeId, String evaluationPeriod, String performanceRating, String comments) {
        this.employeeId = employeeId;
        this.evaluationPeriod = evaluationPeriod;
        this.performanceRating = performanceRating;
        this.comments = comments;
    }

    // Getters and Setters

    public Integer getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Integer evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEvaluationPeriod() {
        return evaluationPeriod;
    }

    public void setEvaluationPeriod(String evaluationPeriod) {
        this.evaluationPeriod = evaluationPeriod;
    }

    public String getPerformanceRating() {
        return performanceRating;
    }

    public void setPerformanceRating(String performanceRating) {
        this.performanceRating = performanceRating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}