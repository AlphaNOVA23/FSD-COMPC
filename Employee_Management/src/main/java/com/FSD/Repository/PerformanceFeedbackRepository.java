package com.FSD.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FSD.Entity.PerformanceFeedbackEntity;

@Repository
public interface PerformanceFeedbackRepository extends JpaRepository<PerformanceFeedbackEntity, Integer> {
    
    // Find all feedback received by a specific employee
    List<PerformanceFeedbackEntity> findByEmployee_EmployeeId(Integer employeeId);
    
    // Find all feedback given BY a specific reviewer
    List<PerformanceFeedbackEntity> findByReviewer_EmployeeId(Integer reviewerId);

    // Keep these for grouping logic
    List<PerformanceFeedbackEntity> findByEvaluationId(Integer evaluationId);
    List<PerformanceFeedbackEntity> findByTrainingId(Integer trainingId);
}