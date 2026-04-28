package com.FSD.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.FSD.Entity.PerformanceReviewEntity;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReviewEntity, Integer> {
}
