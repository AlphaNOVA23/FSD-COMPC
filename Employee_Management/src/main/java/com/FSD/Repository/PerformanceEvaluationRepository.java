package com.FSD.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FSD.Entity.PerformanceEvaluationEntity;

public interface PerformanceEvaluationRepository extends JpaRepository<PerformanceEvaluationEntity, Integer> {
}