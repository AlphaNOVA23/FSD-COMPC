package com.FSD.Repository;

import com.FSD.Entity.TrainingFeedbackEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingFeedbackRepository extends JpaRepository<TrainingFeedbackEntity, Integer> {
    List<TrainingFeedbackEntity> findByTrainingProgram_TrainingId(Integer trainingId);
}
