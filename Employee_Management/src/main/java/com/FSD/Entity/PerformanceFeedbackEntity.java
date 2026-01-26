package com.FSD.Entity;

import java.time.LocalDate;

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
@Table(name = "performance_feedback")
public class PerformanceFeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Integer feedbackId;

    // The Employee receiving the feedback
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    // The Employee giving the feedback (Reviewer)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private EmployeeEntity reviewer;

    @Column(name = "rating", nullable = false)
    private Double rating; // 1.0 to 5.0, for example

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "feedback_date", nullable = false)
    private LocalDate feedbackDate;

    // Kept as loose integers for grouping, as we don't have Evaluation/Training tables yet
    @Column(name = "evaluation_id")
    private Integer evaluationId; 

    @Column(name = "training_id")
    private Integer trainingId;

    public PerformanceFeedbackEntity() {}

    // Getters and Setters

    public Integer getFeedbackId() { return feedbackId; }
    public void setFeedbackId(Integer feedbackId) { this.feedbackId = feedbackId; }

    public EmployeeEntity getEmployee() { return employee; }
    public void setEmployee(EmployeeEntity employee) { this.employee = employee; }

    public EmployeeEntity getReviewer() { return reviewer; }
    public void setReviewer(EmployeeEntity reviewer) { this.reviewer = reviewer; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public LocalDate getFeedbackDate() { return feedbackDate; }
    public void setFeedbackDate(LocalDate feedbackDate) { this.feedbackDate = feedbackDate; }

    public Integer getEvaluationId() { return evaluationId; }
    public void setEvaluationId(Integer evaluationId) { this.evaluationId = evaluationId; }

    public Integer getTrainingId() { return trainingId; }
    public void setTrainingId(Integer trainingId) { this.trainingId = trainingId; }
}