package com.FSD.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "performance_review")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PerformanceReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"performanceReviews", "department", "departmentHeadRole", "responsibilities", "credential", "salary", "positionDetails", "trainings"})
    private EmployeeEntity employee;

    @Column(name = "review_date")
    private LocalDate reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previousreview_id")
    @JsonIgnoreProperties({"previousReview", "employee"})
    private PerformanceReviewEntity previousReview;

    @Column(name = "attendanceid")
    private Integer attendanceId;

    @Column(name = "scorechange")
    private Integer scoreChange;

    public PerformanceReviewEntity() {}

    public PerformanceReviewEntity(EmployeeEntity employee, LocalDate reviewDate, PerformanceReviewEntity previousReview, Integer attendanceId, Integer scoreChange) {
        this.employee = employee;
        this.reviewDate = reviewDate;
        this.previousReview = previousReview;
        this.attendanceId = attendanceId;
        this.scoreChange = scoreChange;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public EmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public PerformanceReviewEntity getPreviousReview() {
        return previousReview;
    }

    public void setPreviousReview(PerformanceReviewEntity previousReview) {
        this.previousReview = previousReview;
    }

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Integer getScoreChange() {
        return scoreChange;
    }

    public void setScoreChange(Integer scoreChange) {
        this.scoreChange = scoreChange;
    }
}
